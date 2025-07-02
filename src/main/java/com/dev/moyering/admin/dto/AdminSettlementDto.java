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
    private String bankType;
    private String bankAccount;          // 지급계좌 정보
    private BigDecimal totalIncome;      // 클래스 당 총 결제 금액 (뷰에서 가져와서 집계)
    private BigDecimal platformFee;      // 클래스 당 정산 수수료 (뷰에서 가져와서 집계)
    private BigDecimal settlementAmount; // 실제 지급액

    public AdminSettlementDto(Integer settlementId, Integer calendarId, Integer hostId, LocalDate settlementDate, LocalDateTime settledAt, String settlementStatus, String bankType, String bankAccount, BigDecimal totalIncome, BigDecimal platformFee, BigDecimal settlementAmount) {
        this.settlementId = settlementId;
        this.calendarId = calendarId;
        this.hostId = hostId;
        this.settlementDate = settlementDate;
        this.settledAt = settledAt;
        this.settlementStatus = settlementStatus;
        this.bankType = bankType;
        this.bankAccount = bankAccount;
        this.totalIncome = totalIncome;
        this.platformFee = platformFee;
        this.settlementAmount = settlementAmount;
    }
}
