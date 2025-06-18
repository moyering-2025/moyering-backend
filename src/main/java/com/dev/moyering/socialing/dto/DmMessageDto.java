package com.dev.moyering.socialing.dto;

import com.dev.moyering.common.entity.User;
import com.dev.moyering.socialing.entity.DmMessage;
import com.dev.moyering.socialing.entity.DmRoom;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Builder
public class DmMessageDto {

    private Long messageId;
    private String content;
    private LocalDateTime sendAt;
    private boolean isRead;

    private Integer dmRoomId;
    private Integer senderId;

    public DmMessage toEntity() {
        DmMessage entity = DmMessage.builder()
                .messageId(messageId)
                .content(content)
                .sendAt(sendAt)
                .isRead(false)
                .dmRoom(DmRoom.builder().roomId(dmRoomId).build())
                .senderId(User.builder().userId(senderId).build())
                .build();
        return entity;
    }
}