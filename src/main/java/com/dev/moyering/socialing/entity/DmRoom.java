package com.dev.moyering.socialing.entity;

import javax.persistence.*;

import com.dev.moyering.user.entity.User;

import com.dev.moyering.socialing.dto.DmRoomDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DmRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user1Id", nullable = false)
    private User user1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user2Id", nullable = false)
    private User user2;

    @Column(columnDefinition = "TEXT")
    private String lastMessage;  // ✅ 가장 최근 메시지 내용

    private LocalDateTime lastSentAt;  // ✅ 가장 최근 메시지 시간

    public DmRoomDto toDto() {
        return DmRoomDto.builder()
                .roomId(roomId)
                .user1Id(user1.getUserId())
                .user1Nickname(user1.getUsername())
                .user1Profile(user1.getProfile())
                .user2Id(user2.getUserId())
                .user2Nickname(user2.getUsername())
                .user2Profile(user2.getProfile())
                .lastMessage(lastMessage)
                .lastSendAt(lastSentAt)
                .build();
    }
}
