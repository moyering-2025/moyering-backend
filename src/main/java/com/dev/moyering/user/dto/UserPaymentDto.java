package com.dev.moyering.user.dto;

import com.dev.moyering.admin.entity.AdminCoupon;
import com.dev.moyering.classring.entity.UserCoupon;
import com.dev.moyering.host.entity.ClassCalendar;
import com.dev.moyering.host.entity.ClassRegist;
import com.dev.moyering.host.entity.HostClass;
import com.dev.moyering.user.entity.UserPayment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor


public class UserPaymentDto {
    private Integer paymentId; // 결제 id
    private String orderNo; //주문번호
    private Integer amount; // 결제금액
    private String paymentType; // 결제유형 (카드결제, 간편결제)
    private LocalDateTime paidAt; // 결제일
    private String status; // 상태 (주문, 취소, 환불)
    private ClassRegist classRegist; // 수강생 id
    private ClassCalendar classCalendar;  // 클래스 일정 id
    private UserCoupon userCoupon; // 사용한 쿠폰 id
    private Integer classPrice; // 클래스 금액
    private String couponType; //사용한 쿠폰 유형
    private String discountType; //쿠폰 할인 유형
    private BigDecimal platformFee; // 수수료

    public UserPaymentDto(Integer paymentId, String orderNo, Integer amount, String paymentType, LocalDateTime paidAt, String status, ClassRegist classRegist, ClassCalendar classCalendar, UserCoupon userCoupon, Integer classPrice, String couponType, String discountType, BigDecimal platformFee) {
        this.paymentId = paymentId;
        this.orderNo = orderNo;
        this.amount = amount;
        this.paymentType = paymentType;
        this.paidAt = paidAt;
        this.status = status;
        this.classRegist = classRegist;
        this.classCalendar = classCalendar;
        this.userCoupon = userCoupon;
        this.classPrice = classPrice;
        this.couponType = couponType;
        this.discountType = discountType;
        this.platformFee = platformFee;
    }

    // DTO -> Entity 변환
    public UserPayment toEntity() {
        return UserPayment.builder()
                .paymentId(this.paymentId)
                .orderNo(this.orderNo)
                .amount(this.amount)
                .paymentType(this.paymentType)
                .paidAt(this.paidAt)
                .status(this.status)
                .classRegist(this.classRegist)
                .classCalendar(this.classCalendar)
                .userCoupon(this.userCoupon)
                .classPrice(this.classPrice) // 클래스 금액
                .couponType(this.couponType) //사용한 쿠폰 유형
                .discountType(this.discountType) //쿠폰 할인 유형
                .platformFee(this.platformFee) // 수수료
                .build();
    }
}