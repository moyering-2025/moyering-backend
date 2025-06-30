package com.dev.moyering.admin.dto;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@Slf4j

// 수강생의 결제내역 조회이므로 수수료 X
public class AdminPaymentDto {
    private Integer paymentId;      // 결제번호
    private String orderNo;         // 주문번호
    private String studentId;       // 수강생 로그인 아이디
    private String className;       // 클래스명
    private Integer classAmount;    // 클래스금액
    private String couponType;      // 쿠폰 유형
    private String discountType;    // 할인 유형
    private Integer discountAmount; // 할인금액 / 비율 (가변)
    private Integer calculatedDiscountAmount; // 할인금액 (원)
    private Integer totalAmount;    // 총 결제 금액
    private LocalDateTime payDate;  // 결제일시
    private String paymentType;     // 결제 유형
    private String status;          // 결제 상태

    public AdminPaymentDto(Integer paymentId, String orderNo, String studentId,
                           String className, Integer classAmount, String couponType,
                           String discountType, Integer discountAmount,  Integer calculatedDiscountAmount, Integer totalAmount,
                           LocalDateTime payDate, String paymentType, String status) {
        this.paymentId = paymentId;
        this.orderNo = orderNo;
        this.studentId = studentId;
        this.className = className;
        this.classAmount = classAmount;
        this.couponType = couponType;
        this.discountType = discountType;
        this.discountAmount = discountAmount;
        this.calculatedDiscountAmount = calculatedDiscountAmount;
        this.totalAmount = totalAmount;
        this.payDate = payDate;
        this.paymentType = paymentType;
        this.status = status;
    }
}