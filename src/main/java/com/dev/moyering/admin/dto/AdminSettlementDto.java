package com.dev.moyering.admin.dto;


import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
@Builder
@Slf4j
public class AdminSettlementDto {

    // 기본 정산 정보
    private Integer settlementId; // 정산 id
    private Integer calendarId; // 클래스 일정 Id
    private Integer hostId; // 강사 id

    // 정산 정보
    private LocalDate settlementDate;    // 정산 예정일
    private LocalDateTime settledAt;     // 실제 지급일
    private String settlementStatus;     // 정산 상태 (PENDING/COMPLETED/CANCELLED)
    private String bankName;        // 은행 정보
    private String accNum;          // 지급계좌 정보
    private Integer settleAmountToDo;      // 정산예정 금액
    private Integer settlementAmount; // 실제 지급액

    public AdminSettlementDto(Integer settlementId, Integer calendarId, Integer hostId, LocalDate settlementDate, LocalDateTime settledAt, String settlementStatus, String bankName, String accNum, Integer settleAmountToDo, Integer settlementAmount) {
        this.settlementId = settlementId;
        this.calendarId = calendarId;
        this.hostId = hostId;
        this.settlementDate = settlementDate;
        this.settledAt = settledAt;
        this.settlementStatus = settlementStatus;
        this.bankName = bankName;
        this.accNum = accNum;
        this.settleAmountToDo = settleAmountToDo;
        this.settlementAmount = settlementAmount;
    }
}