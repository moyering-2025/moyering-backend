package com.dev.moyering.socialing.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.dev.moyering.common.entity.User;

import com.dev.moyering.socialing.dto.DmMessageDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DmMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    @Column(nullable = false)
    private String content;
    private LocalDateTime sendAt;

    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean isRead = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dmRoomId", nullable = false)
    private DmRoom dmRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "senderId", nullable = false)
    private User senderId;

    public DmMessageDto toDto() {
        return DmMessageDto.builder()
                .messageId(messageId)
                .content(content)
                .sendAt(LocalDateTime.now())
                .isRead(false)
                .dmRoomId(dmRoom.getRoomId())
                .senderId(senderId.getUserId())
                .build();
    }
}
