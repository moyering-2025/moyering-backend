package com.dev.moyering.socialing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RedisDmMessage implements Serializable {

    private Long messageId;       // DB와 동기화될 Id
    private Integer senderId;     // 보낸 사람
    private String content;       // 메시지 내용
    private LocalDateTime sendAt; // 보낸 시간
    private Boolean isRead;
    private Integer roomId;
}
