package com.dev.moyering.user.entity;

import com.dev.moyering.admin.dto.AdminCouponDto;
import com.dev.moyering.classring.entity.UserCoupon;
import com.dev.moyering.host.entity.ClassCalendar;
import com.dev.moyering.host.entity.ClassRegist;
import com.dev.moyering.user.dto.UserPaymentDto;
import lombok.*;
import org.springframework.data.relational.core.sql.In;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@Table(name = "payment") // 결제 내역 테이블
public class UserPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer paymentId; // 결제 id

    @Column(unique = true)
    private String orderNo; // 주문 id

    @Column
    private Integer amount; // 결제금액

    @Column
    private String paymentType; // 결제유형

    @Column
    private LocalDateTime paidAt; // 결제일

    @Column
    private String status; // 상태 (주문, 취소)
    
    @Column
    private LocalDateTime canceledAt; //취소시간
    

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id") // 수강생 id
    private ClassRegist classRegist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calendar_id") // 클래스 일정 id
    private ClassCalendar classCalendar;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uc_id") // 사용한 쿠폰 id
    private UserCoupon userCoupon;

    @Column
    private Integer classPrice; // 가격

    @Column
    private String couponType; //사용한 쿠폰 유형

    @Column
    private String discountType; //쿠폰 할인 유형

    @Column
    // 관리자 쿠폰(MG) 사용하면 => 결제금액의 10%, // 강사쿠폰이면 => 클래스 원가의 10%
    // 관리자 쿠폰 사용하면, 정산할 때 (클래스 강의 당 원가 합) - 수수료 합, 강사쿠폰일 시 정산할 때 결제금액 합 - 수수료 합
    private BigDecimal platformFee;



@Builder
    public UserPayment(Integer paymentId, String orderNo, Integer amount, String paymentType, LocalDateTime paidAt, String status, LocalDateTime canceledAt, ClassRegist classRegist, ClassCalendar classCalendar, UserCoupon userCoupon, Integer classPrice, String couponType, String discountType, BigDecimal platformFee) {
        this.paymentId = paymentId;
        this.orderNo = orderNo;
        this.amount = amount;
        this.paymentType = paymentType;
        this.paidAt = paidAt;
        this.status = status;
        this.canceledAt = canceledAt;
        this.classRegist = classRegist;
        this.classCalendar = classCalendar;
        this.userCoupon = userCoupon;
        this.classPrice = classPrice;
        this.couponType = couponType;
        this.discountType = discountType;
        this.platformFee = platformFee;
    }

    
    public void approve(ClassRegist regist, LocalDateTime paidAt) {
        this.status = "결제완료";
        this.paidAt = paidAt;
        this.classRegist = regist;
    }
}


