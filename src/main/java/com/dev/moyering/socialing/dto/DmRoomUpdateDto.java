package com.dev.moyering.socialing.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DmRoomUpdateDto {

    private Integer roomId;
    private String lastMessage;
    private LocalDateTime lastSendAt;
}
