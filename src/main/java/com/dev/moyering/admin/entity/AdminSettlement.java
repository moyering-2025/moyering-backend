package com.dev.moyering.admin.entity;


import com.dev.moyering.admin.dto.AdminSettlementDto;
import com.dev.moyering.host.entity.ClassCalendar;
import com.dev.moyering.user.entity.UserPayment;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(exclude = { "classCalendar"})
@Table(name = "settlement")
public class AdminSettlement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer settlementId; // 정산 ID

    @Column(nullable = false)
    private Integer hostId; // 강사 ID

    @Column(nullable = false)
    private LocalDate settlementDate; // 정산 예정일

    @Column
    private LocalDateTime settledAt; // 실제 지급일

    @Column(nullable = false, length = 20)
    private String settlementStatus; // 정산 상태 (PENDING/COMPLETED/CANCELLED)

    @Column
    private String bankType;

    @Column
    private String bankAccount; // 지급계좌

    @Column(precision = 15, scale = 2)
    private BigDecimal totalIncome; // 총 수입 (뷰에서 가져온 값)

    @Column(precision = 15, scale = 2)
    private BigDecimal platformFee; // 정산 수수료 (뷰에서 계산된 값)

    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal settlementAmount; // 실제 지급액 (뷰에서 계산된 값)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    private UserPayment userPayment; // 결제 정보

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calendar_id", nullable = false)
    private ClassCalendar classCalendar; // 클래스 일정 정보

    @Builder
    public AdminSettlement(Integer settlementId, Integer hostId, LocalDate settlementDate,
                           LocalDateTime settledAt, String settlementStatus, String bankType,  String bankAccount,
                           BigDecimal totalIncome, BigDecimal platformFee, BigDecimal settlementAmount,
                           ClassCalendar classCalendar) {
        this.settlementId = settlementId;
        this.classCalendar = classCalendar;
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

    /**
     * 엔티티 → DTO 변환
     */
    public AdminSettlementDto toDto() {
        return AdminSettlementDto.builder()
                .settlementId(settlementId)
                .calendarId(classCalendar != null ? classCalendar.getCalendarId() : null)
                .hostId(hostId)
                .settlementDate(settlementDate)
                .settledAt(settledAt)
                .settlementStatus(settlementStatus)
                .bankType(bankType)
                .bankAccount(bankAccount)
                .totalIncome(totalIncome)
                .platformFee(platformFee)
                .settlementAmount(settlementAmount)
                .build();
    }

    /**
     * 정산 상태 업데이트
     */
    public void updateStatus(String status) {
        this.settlementStatus = status;
        if ("COMPLETED".equals(status)) {
            this.settledAt = LocalDateTime.now();
        }
    }
}