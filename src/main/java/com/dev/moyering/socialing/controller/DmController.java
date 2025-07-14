package com.dev.moyering.socialing.controller;

import com.dev.moyering.auth.PrincipalDetails;
import com.dev.moyering.socialing.dto.DmRoomDto;
import com.dev.moyering.socialing.entity.DmRoom;
import com.dev.moyering.socialing.repository.DmRoomRepository;
import com.dev.moyering.socialing.service.DmChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user/dm")
@RequiredArgsConstructor
public class DmController {

    private final DmChatService dmChatService;
    private final DmRoomRepository dmRoomRepository;

    @PostMapping("/mark-read")
    public String markMessagesAsRead(@RequestParam Integer roomId, @RequestParam Integer userId) {
        dmChatService.markMessagesAsRead(roomId, userId);
        return "읽음 처리 완료";
    }

    @GetMapping("/rooms")
    public List<DmRoomDto> getMyDmRooms(@AuthenticationPrincipal PrincipalDetails principal) {

        Integer userId = principal.getUser().getUserId();
        List<DmRoom> myRooms = dmRoomRepository.findAllByUser(userId);

        return myRooms.stream()
                .map(room -> room.toDto(userId)) // ✅ myId 전달
                .collect(Collectors.toList());
    }
}
