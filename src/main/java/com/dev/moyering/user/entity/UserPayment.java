package com.dev.moyering.user.entity;

import com.dev.moyering.admin.dto.AdminCouponDto;
import com.dev.moyering.classring.entity.UserCoupon;
import com.dev.moyering.host.entity.ClassCalendar;
import com.dev.moyering.host.entity.ClassRegist;
import com.dev.moyering.user.dto.UserPaymentDto;
import lombok.*;
import org.springframework.data.relational.core.sql.In;

import javax.persistence.*;
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

    @Column
    private String orderNo; // 주문 id

    @Column
    private Integer amount; // 결제금액

    @Column
    private String paymentType; // 결제유형

    @Column
    private LocalDateTime paidAt; // 결제일

    @Column
    private String status; // 상태 (주문, 취소, 환불)
    
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


    @Builder
    public UserPayment(Integer paymentId, String orderNo, Integer amount, String paymentType, LocalDateTime paidAt, String status, ClassRegist classRegist, ClassCalendar classCalendar, UserCoupon userCoupon) {
        this.paymentId = paymentId;
        this.orderNo = orderNo;
        this.amount = amount;
        this.paymentType = paymentType;
        this.paidAt = paidAt;
        this.status = status;
        this.classRegist = classRegist;
        this.classCalendar = classCalendar;
        this.userCoupon = userCoupon;
    }
}


