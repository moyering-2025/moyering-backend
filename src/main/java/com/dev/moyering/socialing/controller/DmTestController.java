package com.dev.moyering.socialing.controller;

import com.dev.moyering.socialing.dto.RedisDmMessage;
import com.dev.moyering.socialing.entity.DmMessage;
import com.dev.moyering.socialing.entity.DmRoom;
import com.dev.moyering.socialing.repository.DmRoomRepository;
import com.dev.moyering.socialing.service.DmChatService;
import com.dev.moyering.user.entity.User;
import com.dev.moyering.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dm-test")
public class DmTestController {

    private final DmChatService dmChatService;
    private final DmRoomRepository dmRoomRepository;
    private final UserRepository userRepository;

    @PostMapping("/save")
    public String saveTest(@RequestParam Integer roomId, @RequestBody RedisDmMessage message) {
        dmChatService.saveMessage(roomId, message);
        return "saved";
    }

    @GetMapping("/recent")
    public List<RedisDmMessage> recentTest(@RequestParam Integer roomId,
                                           @RequestParam(defaultValue = "0") Integer start,
                                           @RequestParam(defaultValue = "10") int count) {
        return dmChatService.getRecentMessages(roomId,start, count);
    }

    @PostMapping("/send")
    public String sendMessage(@RequestParam Integer roomId,
                              @RequestParam Integer senderId,
                              @RequestBody String content) {

        DmRoom room = dmRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("채팅방 없음"));

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("유저 없음"));

        // 1) MariaDB Entity
        DmMessage entity = DmMessage.builder()
                .dmRoom(room)
                .senderId(sender)
                .content(content)
                .sendAt(LocalDateTime.now())
                .isRead(false)
                .build();

        // 2) Redis DTO
        RedisDmMessage dto = RedisDmMessage.builder()
                .messageId(null) // 나중에 동기화
                .senderId(senderId)
                .content(content)
                .sendAt(LocalDateTime.now())
                .isRead(false)
                .build();

        dmChatService.saveMessageBoth(roomId, entity, dto);

        return "ok";
    }
}
