package com.dev.moyering.admin.entity;
import com.dev.moyering.admin.dto.AdminSettlementDto;
import com.dev.moyering.host.entity.ClassCalendar;
import lombok.*;
import javax.persistence.*;
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
    private String settlementStatus; // 정산 상태 (WT/CP/RQ)

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

    /*** 정산 완료 처리*/
    public AdminSettlement completeSettlement() {
        return AdminSettlement.builder()
                .settlementId(this.settlementId)
                .classCalendar(this.classCalendar)
                .hostId(this.hostId)
                .settlementDate(this.settlementDate)
                .settlementStatus("CP") //완료 상태로 변경
                .settledAt(LocalDateTime.now()) // 정산 완료일 설정
                .settlementAmount(this.settleAmountToDo) // 정산예정금액 -> 정산 금액
                .bankName(this.bankName)
                .accNum(this.accNum)
                .settleAmountToDo(this.settlementAmount)
                .build();
    }
       }

