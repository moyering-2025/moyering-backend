package com.dev.moyering.socialing.dto;

import com.dev.moyering.common.entity.User;
import com.dev.moyering.socialing.entity.DmRoom;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    public DmRoom toEntity() {
        DmRoom entity = DmRoom.builder()
                .roomId(roomId)
                .user1(User.builder()
                        .userId(user1Id)
                        .id(user1Nickname)
                        .profile(user1Profile)
                        .build())
                .user2(User.builder()
                        .userId(user2Id)
                        .id(user2Nickname)
                        .profile(user2Profile)
                        .build())
                .build();
        return entity;
    }

}
