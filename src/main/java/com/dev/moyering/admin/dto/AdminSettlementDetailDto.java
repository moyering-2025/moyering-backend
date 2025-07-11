package com.dev.moyering.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class AdminSettlementDetailDto {
    // 정산 기본 정보
    private Integer settlementId; // 정산 ID
    private String className; // 클래스명
    private String hostName; // 강사명
    private Integer settleAmountToDo; // 정산 예정 금액

    // 정산 통계 정보
    private Integer totalPaymentCount; // 총 결제 건수
    private Integer totalClassAmount; // 총 클래스 금액
    private Integer totalDiscountAmount; // 총 할인 금액
    private Integer totalSettlementAmount; // 실제 정산 금액
    private Integer completedPaymentCount; // 결제 완료 건수
    private Integer refundedPaymentCount; // 환불 건수

    // 결제 상세 정보 (결제 목록용)
    private Integer paymentId; // 결제 ID
    private String orderNo; // 주문번호
    private String studentId; // 수강생 ID
    private Integer classAmount; // 클래스 금액 (개별)
    private String couponType; // 쿠폰 타입
    private String discountType; // 할인 타입
    private Integer discountAmount; // 할인 금액
    private Integer totalAmount; // 실제 결제 금액
    private LocalDateTime payDate; // 결제일시
    private String paymentType; // 결제 유형
    private String status; // 결제 상태

    // 정산 요약 정보만 담는 생성자
    public AdminSettlementDetailDto(Integer settlementId, String className, String hostName,
                                    Integer settleAmountToDo, Integer totalPaymentCount,
                                    Integer totalClassAmount, Integer totalDiscountAmount,
                                    Integer totalSettlementAmount, Integer completedPaymentCount,
                                    Integer refundedPaymentCount) {
        this.settlementId = settlementId;
        this.className = className;
        this.hostName = hostName;
        this.settleAmountToDo = settleAmountToDo;
        this.totalPaymentCount = totalPaymentCount;
        this.totalClassAmount = totalClassAmount;
        this.totalDiscountAmount = totalDiscountAmount;
        this.totalSettlementAmount = totalSettlementAmount;
        this.completedPaymentCount = completedPaymentCount;
        this.refundedPaymentCount = refundedPaymentCount;
    }

    // 결제 상세 정보만 담는 생성자
    public AdminSettlementDetailDto(Integer paymentId, String orderNo, String studentId,
                                    Integer classAmount, String couponType, String discountType,
                                    Integer discountAmount, Integer totalAmount, LocalDateTime payDate,
                                    String paymentType, String status) {
        this.paymentId = paymentId;
        this.orderNo = orderNo;
        this.studentId = studentId;
        this.classAmount = classAmount;
        this.couponType = couponType;
        this.discountType = discountType;
        this.discountAmount = discountAmount;
        this.totalAmount = totalAmount;
        this.payDate = payDate;
        this.paymentType = paymentType;
        this.status = status;
    }
}