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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calendar_id", nullable = false)
    private ClassCalendar classCalendar; // 클래스 일정 정보

    @Column(nullable = false)
    private Integer hostId; // 강사 ID

    @Column(nullable = false)
    private LocalDate settlementDate; // 정산 예정일

    @Column
    private LocalDateTime settledAt; // 실제 지급일

    @Column(nullable = false)
    private String settlementStatus; // 정산 상태 (PENDING/COMPLETED/CANCELLED)

    @Column
    private String bankName;

    @Column
    private String accNum; // 지급계좌

    @Column
    private Integer settleAmountToDo; // 정산예정금액


    @Column
    private Integer settlementAmount; // 실제 지급액


    @Builder
    public AdminSettlement(Integer settlementId, ClassCalendar classCalendar, Integer hostId, LocalDate settlementDate, LocalDateTime settledAt, String settlementStatus, String bankName, String accNum, Integer settleAmountToDo, Integer settlementAmount) {
        this.settlementId = settlementId;
        this.classCalendar = classCalendar;
        this.hostId = hostId;
        this.settlementDate = settlementDate;
        this.settledAt = settledAt;
        this.settlementStatus = settlementStatus;
        this.bankName = bankName;
        this.accNum = accNum;
        this.settleAmountToDo = settleAmountToDo;
        this.settlementAmount = settlementAmount;
    }


    /*** 엔티티 → DTO 변환*/
    public AdminSettlementDto toDto() {
        return AdminSettlementDto.builder()
                .settlementId(settlementId)
                .calendarId(classCalendar != null ? classCalendar.getCalendarId() : null)
                .hostId(hostId)
                .settlementDate(settlementDate)
                .settledAt(settledAt)
                .settlementStatus(settlementStatus)
                .bankName(bankName)
                .accNum(accNum)
                .settleAmountToDo(settleAmountToDo)
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