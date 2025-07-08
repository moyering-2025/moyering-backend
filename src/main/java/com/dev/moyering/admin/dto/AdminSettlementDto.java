package com.dev.moyering.admin.dto;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Builder
@Setter
@Slf4j

public class AdminSettlementDto {

    // 기본 정산 정보
    private Integer settlementId; // 정산 id
    private Integer calendarId; // 클래스 일정 Id
    private Integer hostId; // 강사 id

    private String username; // 강사 로그인 아이디
    private String hostName; // 강사 이름 (user.name)
    private String className; // 클래스명

//    private Integer totalIncome; // 강의 당 총 순수 입금액
//    private Integer totalDiscount; // 강의 당 총 할인 금액
//    private Integer totalPlatformFee; // 총 플랫폼 수수료

    // 정산 정보
    private LocalDate settlementDate;    // 정산 예정일
    private LocalDateTime settledAt;     // 실제 지급일
    private String settlementStatus;     // 정산 상태 (WT, CP, RQ)
    private String bankName;        // 은행 정보
    private String accNum;          // 지급계좌 정보
    private Integer settleAmountToDo;      // 정산예정 금액
    private Integer settlementAmount; // 실제 지급액

    public AdminSettlementDto(Integer settlementId, Integer calendarId, Integer hostId, String username, String hostName, String className, LocalDate settlementDate, LocalDateTime settledAt, String settlementStatus, String bankName, String accNum, Integer settleAmountToDo, Integer settlementAmount) {
        this.settlementId = settlementId;
        this.calendarId = calendarId;
        this.hostId = hostId;
        this.username = username;
        this.hostName = hostName;
        this.className = className;
        this.settlementDate = settlementDate;
        this.settledAt = settledAt;
        this.settlementStatus = settlementStatus;
        this.bankName = bankName;
        this.accNum = accNum;
        this.settleAmountToDo = settleAmountToDo;
        this.settlementAmount = settlementAmount;
    }
}