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
    private Integer amount; // 결제금액

    @Column
    private String paymentType; // 결제유형

    @Column
    private Date paidAt; // 결제일

    @Column
    private String status; // 상태 (주문, 취소, 환불)

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
    private Integer platformFee; // 수수료
    // 관리자쿠폰일 경우, 강의 원가의 10%, 강사쿠폰일 경우, 전체 결제금액의 10%

    @Builder
    public UserPayment(Integer paymentId, Integer amount, String paymentType, Date paidAt, String status, ClassRegist classRegist, ClassCalendar classCalendar, UserCoupon userCoupon, Integer platformFee) {
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

    // Entity -> DTO 변환
    public UserPaymentDto toDto() {
        return UserPaymentDto.builder()
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
