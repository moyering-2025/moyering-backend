package com.dev.moyering.redis;

import com.dev.moyering.socialing.dto.RedisDmMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisSubscriber implements MessageListener {

    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {

        String channel = new String(message.getChannel());
        String body = new String(message.getBody());
        log.info("🔔 Redis pub/sub 메시지 수신 → 채널: {}, 내용: {}", channel, body);

        // ✅ 문자열이면 특별 처리
        if ("readUpdated".equals(body.replace("\"", ""))) { // Redis에서 String은 "..." 감싸짐
            log.info("✅ 'readUpdated' 이벤트 처리, 실시간 갱신 알림");
            messagingTemplate.convertAndSend("/topic/" + channel, "readUpdated");
            return;
        }

        // 여기서 WebSocket 세션에 push 하면 됨
        RedisDmMessage msg = null;
        try {
            msg = objectMapper.readValue(body, RedisDmMessage.class);

            // sendAt null 방어 (혹시 Redis 직렬화 깨졌을 경우)
            if (msg.getSendAt() == null) {
                msg.setSendAt(LocalDateTime.now());
            }

            if (channel.equals("dmRoomUpdates")) {
                messagingTemplate.convertAndSend("/topic/dmRoomUpdates", msg);
            } else {
                messagingTemplate.convertAndSend("/topic/" + channel, msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("JSON 파싱 실패", e);
        }
    }
}
