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

import java.sql.Date;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor

public class UserPaymentDto {
    private Integer paymentId; // 결제 id
    private Integer amount; // 결제금액
    private String paymentType; // 결제유형 (카드결제, 간편결제)
    private Date paidAt; // 결제일
    private String status; // 상태 (주문, 취소, 환불)
    private ClassRegist classRegist; // 수강생 id
    private ClassCalendar classCalendar;  // 클래스 일정 id
    private UserCoupon userCoupon; // 사용한 쿠폰 id
    private Integer platformFee; // 수수료

    public UserPaymentDto(Integer paymentId, Integer amount, String paymentType, Date paidAt, String status, ClassRegist classRegist, ClassCalendar classCalendar, UserCoupon userCoupon, Integer platformFee) {
        this.paymentId = paymentId;
        this.amount = amount;
        this.paymentType = paymentType;
        this.paidAt = paidAt;
        this.status = status;
        this.classRegist = classRegist;
        this.classCalendar = classCalendar;
        this.userCoupon = userCoupon;
        this.platformFee = platformFee;
    }

    // DTO -> Entity 변환
    public UserPayment toEntity() {
        return UserPayment.builder()
                .paymentId(this.paymentId)
                .amount(this.amount)
                .paymentType(this.paymentType)
                .paidAt(this.paidAt)
                .status(this.status)
                .classRegist(this.classRegist)
                .classCalendar(this.classCalendar)
                .userCoupon(this.userCoupon)
                .platformFee(this.platformFee)
                .build();
    }
}