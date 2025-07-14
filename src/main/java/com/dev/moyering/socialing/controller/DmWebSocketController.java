package com.dev.moyering.socialing.controller;

import com.dev.moyering.redis.RedisPublisher;
import com.dev.moyering.socialing.dto.DmSendRequestDto;
import com.dev.moyering.socialing.service.DmChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@Controller
@RequiredArgsConstructor
public class DmWebSocketController {

    private final RedisPublisher redisPublisher;
    private final DmChatService dmChatService;

//    @MessageMapping("/dm.send")
//    public void handleMessage(@Payload RedisDmMessage message,
//                              @Header("roomId") Integer roomId,
//                              @Header("senderId") Integer senderId,
//                              @Header("receiverId") Integer receiverId) {
//
//        // 방 없으면 자동 생성
//        if (roomId == null || roomId == 0) {
//            roomId = dmChatService.createRoomIfNotExist(senderId, receiverId);
//        }
//
//        // publish
//        redisPublisher.publish("dmRoom:" + roomId, message);
//
//        DmRoomUpdateDto updateDto = DmRoomUpdateDto.builder()
//                .roomId(roomId)
//                .lastMessage(message.getContent())
//                .lastSendAt(message.getSendAt())
//                .build();
//        redisPublisher.publish("dmRoomUpdates", updateDto);
//    }
//@PostMapping("/user/send")
//public String sendMessage(@RequestParam Integer senderId,
//                          @RequestParam Integer receiverId,
//                          @RequestBody String content,
//                          @RequestParam(required = false) Integer roomId) {
//    dmChatService.sendDm(senderId, receiverId, content, roomId);
//    return "ok";
//}
//@PostMapping("/user/send")
//public String sendMessage(@RequestBody DmSendRequestDto req) {
//
//    log.info("✅ /user/send API called with req={}", req.getSenderId(), req.getReceiverId(), req.getRoomId(), req.getContent());
//
//    dmChatService.sendDm(req.getSenderId(), req.getReceiverId(), req.getContent(), req.getRoomId());
//    return "ok";
//}

    @MessageMapping("/dm.send")
    public void sendMessage(@Payload DmSendRequestDto req) {
        log.info("🛠 ws sendMessage called with: {}", req);
        dmChatService.sendDm(req.getSenderId(), req.getReceiverId(), req.getContent(), req.getRoomId());
    }
}
