package com.dev.moyering.admin.dto;


import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
public class AdminSettlementDto {
    // 정산내역페이지 따로 => 정산 완료된 것만
    // 미정산인 클래스 내역, 요청받은 정산 내역 보여주기!
    // 일단 이거는 정산 내역만 보여주자

    // 정산완료, 미정산, 이번주 정산해야할 내역 (필터로 걸기)
    // 정산 기준 => 검색, 정산일 , 상태관리 탭으로 (미정산, 클래스 종료 후 7일 이후 것만 띄우기) => 미정산
    // 정산내역 안에, 예정인 것도 보여줘야할까 ?
    // 정산금액 - 정산받을 id, 클래스명, 클래스금액, 수강확정인원, 총 클래스 금액, 쿠폰 금액, 수수료, 정산지급액,  정산예정일, 정산일
    // classRegist에서


    // 기본 정산 정보
    private Integer settlementId; // 정산 id
    private Integer calendarId; // 클래스 일정 Id
    private Integer hostId; // 강사 id
    private Integer paymentId;

    // 표시용 정보
    private String hostName; // 강사명 (user.username)
    private String className; // 클래스명 (hostClass.name)
    private LocalDate classDate; // 클래스 일자 (classCalendar.class_start)
    private BigDecimal classPrice; // 클래스 원가 (hostClass.price)

    private LocalDate settlementDate; // 정산 예정일 (클래스일 + 7일)
    private LocalDateTime settledAt; // 실제 지급일
    private String settlementStatus; // 정산 상태 (PENDING/COMPLETED/CANCELLED)

    private String bankType; // 지급 은행 !
    private String bankAccount; //지급계좌 정보


    // 계산 금액 정보
    private Integer confirmedStudentCount;   // 수강 확정 인원
    private BigDecimal totalClassAmount;     // 총 클래스 금액 (원가 × 인원)
    private BigDecimal totalPaymentAmount;   // 실제 결제된 금액
    private BigDecimal totalCouponAmount;    // 쿠폰 할인 총액
    private BigDecimal totalIncome;          // 총 수입 (결제 + 쿠폰)
    private BigDecimal platformFee;          // 정산 수수료
    private BigDecimal settlementAmount;     // 실제 지급액


    // 정산 수수료
    // 강사 쿠폰 사용 기록이 있을 경우
    // (클래스 원가 X 수강생 수)  X 10%가 수수료 !
    // 관리자 쿠폰 사용기록이 있을 경우
    // (클래스 원가 X 수강생 수) - 쿠폰 총 할인금액) = 결국 결제금액(입금액)의 X 10%가 수수료
    public AdminSettlementDto(Integer settlementId, Integer calendarId, Integer hostId,
                              String hostName, String className, LocalDate classDate, BigDecimal classPrice,
                              LocalDate settlementDate, LocalDateTime settledAt, String settlementStatus,
                              String bankAccount, Integer confirmedStudentCount, BigDecimal totalClassAmount,
                              BigDecimal totalPaymentAmount, BigDecimal totalCouponAmount, BigDecimal totalIncome,
                              BigDecimal platformFee, BigDecimal settlementAmount) {
        this.settlementId = settlementId;
        this.calendarId = calendarId;
        this.hostId = hostId;
        this.hostName = hostName;
        this.className = className;
        this.classDate = classDate;
        this.classPrice = classPrice;
        this.settlementDate = settlementDate;
        this.settledAt = settledAt;
        this.settlementStatus = settlementStatus;
        this.bankAccount = bankAccount;
        this.confirmedStudentCount = confirmedStudentCount;
        this.totalClassAmount = totalClassAmount;
        this.totalPaymentAmount = totalPaymentAmount;
        this.totalCouponAmount = totalCouponAmount;
        this.totalIncome = totalIncome;
        this.platformFee = platformFee;
        this.settlementAmount = settlementAmount;

    }
}
