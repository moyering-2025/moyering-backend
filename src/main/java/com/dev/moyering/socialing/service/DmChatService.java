package com.dev.moyering.socialing.service;

import com.dev.moyering.redis.RedisPublisher;
import com.dev.moyering.socialing.dto.DmRoomUpdateDto;
import com.dev.moyering.socialing.dto.RedisDmMessage;
import com.dev.moyering.socialing.entity.DmMessage;
import com.dev.moyering.socialing.entity.DmRoom;
import com.dev.moyering.socialing.repository.DmMessageRepository;
import com.dev.moyering.socialing.repository.DmRoomRepository;
import com.dev.moyering.user.entity.User;
import com.dev.moyering.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DmChatService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final DmMessageRepository dmMessageRepository;
    private final RedisPublisher redisPublisher;
    private final DmRoomRepository dmRoomRepository;
    private static final String ROOM_PREFIX = "dmRoom:";
    private final UserRepository userRepository;

    private final ObjectMapper objectMapper;

    // Redis에 메시지 저장
    public void saveMessage(Integer roomId, RedisDmMessage message) {
        String key = ROOM_PREFIX + roomId;
        redisTemplate.opsForList().leftPush(key, message);
    }

    @Transactional
    public void saveMessageBoth(Integer roomId, DmMessage dmMessage, RedisDmMessage redisDmMessage) {
        // 1) Redis
        String key = ROOM_PREFIX + roomId;
        redisTemplate.opsForList().leftPush(key, redisDmMessage);

        redisTemplate.expire(key, Duration.ofDays(30));

        // 2) MariaDB
        dmMessageRepository.save(dmMessage);

        // lastMessage 자동 갱신
        updateLastMessage(roomId, redisDmMessage.getContent(), redisDmMessage.getSendAt());

        // 🔥 여기서 채팅방 리스트용 pub/sub 알림
        DmRoomUpdateDto updateDto = DmRoomUpdateDto.builder()
                .roomId(roomId)
                .lastMessage(redisDmMessage.getContent())
                .lastSendAt(redisDmMessage.getSendAt())
                .build();
        redisPublisher.publish("dmRoomUpdates", updateDto);
    }

    // 최근 N개 메시지 조회
    public List<RedisDmMessage> getRecentMessages(Integer roomId,int start, int count) {
        String key = ROOM_PREFIX + roomId;
        List<Object> range = redisTemplate.opsForList().range(key, start, start + count - 1);


        if (range == null) return List.of();

        return range.stream()
                .map(o -> objectMapper.convertValue(o, RedisDmMessage.class))
                .collect(Collectors.toList());
    }

    public void sendMessage(Integer roomId, Object message) {
        // 1. Redis List 저장
        redisTemplate.opsForList().leftPush(ROOM_PREFIX + roomId, message);

        // 2. pub/sub publish
        redisPublisher.publish(ROOM_PREFIX + roomId, message);
    }

    @Transactional
    public void updateLastMessage(Integer roomId, String lastMessage, LocalDateTime lastSentAt) {
        DmRoom room = dmRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("채팅방 없음"));
        room.setLastMessage(lastMessage);
        room.setLastSentAt(lastSentAt);
    }
    private void updateRedisMessagesAsRead(Integer roomId, Integer userId) {
        String key = ROOM_PREFIX + roomId;
        List<Object> messages = redisTemplate.opsForList().range(key, 0, -1);
        if (messages == null) return;

        List<RedisDmMessage> updatedMessages = new ArrayList<>();
        for (Object o : messages) {
            RedisDmMessage msg = objectMapper.convertValue(o, RedisDmMessage.class);
            if (!msg.getSenderId().equals(userId) && !Boolean.TRUE.equals(msg.getIsRead())) {
                msg.setIsRead(true); // 상대방 메시지면 읽음 처리
            }
            updatedMessages.add(msg);
        }

        // Redis에 덮어쓰기
        redisTemplate.delete(key);
        for (RedisDmMessage msg : updatedMessages) {
            redisTemplate.opsForList().rightPush(key, msg);
        }
    }
    @Transactional
    public void markMessagesAsRead(Integer roomId, Integer userId) {
        long updatedCount = dmMessageRepository.markMessagesAsRead(roomId, userId);
        log.info("✅ QueryDSL로 {}개의 메시지 읽음 처리 완료", updatedCount);
        updateRedisMessagesAsRead(roomId, userId);

        // ✅ 실시간 업데이트를 위한 pub/sub 알림
        redisPublisher.publish("dmRoom:" + roomId, "readUpdated");
    }

    @Transactional
    public Integer createRoomIfNotExist(Integer userA, Integer userB) {
        if (userA == null || userB == null) {
            throw new IllegalArgumentException("👉 createRoomIfNotExist: userA 또는 userB가 null입니다.");
        }
        Integer user1Id = Math.min(userA, userB);
        Integer user2Id = Math.max(userA, userB);

        Optional<DmRoom> existingRoom = dmRoomRepository
                .findByUser1UserIdAndUser2UserId(user1Id, user2Id);

        if (existingRoom.isPresent()) {
            return existingRoom.get().getRoomId();
        }

        // 없으면 방 새로 생성
        User user1 = userRepository.findById(user1Id)
                .orElseThrow(() -> new RuntimeException("user1 없음"));
        User user2 = userRepository.findById(user2Id)
                .orElseThrow(() -> new RuntimeException("user2 없음"));

        DmRoom newRoom = DmRoom.builder()
                .user1(user1)
                .user2(user2)
                .lastMessage(null)
                .lastSentAt(null)
                .build();

        DmRoom saved = dmRoomRepository.save(newRoom);
        return saved.getRoomId();
    }

    @Transactional
    public void sendDm(Integer senderId, Integer receiverId, String content, Integer roomId) {

        log.info("✅ sendDm called with senderId={}, receiverId={}, roomId={}, content={}", senderId, receiverId, roomId, content);

        if (roomId == null || roomId == 0) {
            roomId = createRoomIfNotExist(senderId, receiverId);
        }

        DmRoom room = dmRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("채팅방 없음"));
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("유저 없음"));

        DmMessage entity = DmMessage.builder()
                .dmRoom(room)
                .senderId(sender)
                .content(content)
                .sendAt(LocalDateTime.now())
                .isRead(false)
                .build();
        RedisDmMessage dto = RedisDmMessage.builder()
                .senderId(senderId)
                .content(content)
                .sendAt(LocalDateTime.now())
                .isRead(false)
                .roomId(roomId)
                .build();

        saveMessageBoth(roomId, entity, dto);

        DmRoomUpdateDto updateDto = DmRoomUpdateDto.builder()
                .roomId(roomId)
                .lastMessage(content)
                .lastSendAt(LocalDateTime.now())
                .build();
        redisPublisher.publish("dmRoomUpdates", updateDto);
        redisPublisher.publish("dmRoom:" + roomId, dto);
    }
}
