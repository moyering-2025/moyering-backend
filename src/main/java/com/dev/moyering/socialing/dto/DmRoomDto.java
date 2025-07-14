package com.dev.moyering.socialing.dto;

import java.time.LocalDateTime;

import com.dev.moyering.socialing.entity.DmRoom;
import com.dev.moyering.user.entity.User;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DmRoomDto {

    private Integer roomId;

    private Integer user1Id;
    private String user1Nickname;
    private String user1Profile;
    private Integer user2Id;
    private String user2Nickname;
    private String user2Profile;

    private String lastMessage;
    private LocalDateTime lastSendAt;

    private String opponentNickname;
    private String opponentProfile;

    public DmRoom toEntity() {
        DmRoom entity = DmRoom.builder()
                .roomId(roomId)
                .user1(User.builder()
                        .userId(user1Id)
                        .username(user1Nickname)
                        .profile(user1Profile)
                        .build())
                .user2(User.builder()
                        .userId(user2Id)
                        .username(user2Nickname)
                        .profile(user2Profile)
                        .build())
                .build();
        return entity;
    }

}
