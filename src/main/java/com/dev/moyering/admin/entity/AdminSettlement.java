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
@ToString(exclude = {"userPayment", "classCalendar"})

@Table(name = "settlement")
public class AdminSettlement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer settlementId; // 정산 id


    @Column
    private Integer hostId; // 강사 id

    @Column
    private LocalDate settlementDate; // 정산 예정일

    @Column
    private LocalDateTime settledAt; // 실제 지급일

    @Column
    private String settlementStatus; // 정산 상태

    @Column
    private String bankAccount; //지급계좌

    @Column
    private BigDecimal totalIncome; // 총 입금액

    @Column
    private BigDecimal platformFee; // 정산 수수료

    @Column
    private BigDecimal settlementAmount; // 실제 지급액

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paymentId")  // 결제 id
    private UserPayment userPayment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calendarId") // 클래스 일정 id
    private ClassCalendar classCalendar;

    @Builder
    public AdminSettlement(Integer settlementId, Integer hostId, LocalDate settlementDate, LocalDateTime settledAt, String settlementStatus, String bankAccount, BigDecimal totalIncome, BigDecimal platformFee, BigDecimal settlementAmount, UserPayment userPayment, ClassCalendar classCalendar) {
        this.settlementId = settlementId;
        this.hostId = hostId;
        this.settlementDate = settlementDate;
        this.settledAt = settledAt;
        this.settlementStatus = settlementStatus;
        this.bankAccount = bankAccount;
        this.totalIncome = totalIncome;
        this.platformFee = platformFee;
        this.settlementAmount = settlementAmount;
        this.userPayment = userPayment;
        this.classCalendar = classCalendar;
    }


    // 엔티티 -> toDto
    public AdminSettlementDto toDto() {
        AdminSettlementDto dto = AdminSettlementDto.builder()
                .settlementId(settlementId)
                .hostId(hostId)
                .settlementDate(settlementDate)
                .settledAt(settledAt)
                .settlementStatus(settlementStatus)
                .bankAccount(bankAccount)
                .totalIncome(totalIncome)
                .platformFee(platformFee)
                .settlementAmount(settlementAmount)
                .paymentId(userPayment != null ? userPayment.getPaymentId() : null)
                .calendarId(classCalendar != null ? classCalendar.getCalendarId() : null)
                .build();
        return dto;
    }
}