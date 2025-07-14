package com.dev.moyering.socialing.dto;

import lombok.Data;

@Data
public class DmSendRequestDto {
    private Integer senderId;
    private Integer receiverId;
    private Integer roomId;
    private String content;
}
