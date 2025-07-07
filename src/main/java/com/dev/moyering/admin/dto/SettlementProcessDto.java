package com.dev.moyering.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SettlementProcessDto {
    private Integer calendarId;
    private Integer hostId;
    private String bankName;
    private String accNum;
    private Integer settleAmountToDo;
}
