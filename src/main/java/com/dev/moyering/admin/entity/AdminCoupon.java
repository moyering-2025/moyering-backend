package com.dev.moyering.admin.entity;

import java.time.LocalDateTime;

import javax.persistence.*;

import com.dev.moyering.admin.dto.AdminNoticeDto;
import com.dev.moyering.host.entity.ClassCalendar;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@Table(name="coupon")
public class AdminCoupon {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer couponId;

    @Column(nullable = false)
    private String couponType;

    @Column(nullable = false)
    private String discountType; // 비율이면 'RT', 금액할인이면 'AMT'

    @Column(nullable = false)
    private Integer discount; // 예: 10 (%), 5000 (금액)

    private LocalDateTime validFrom; //쿠폰 시작일

    private LocalDateTime validUntil; //쿠폰 종료일

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String couponName;

    @ManyToOne
    @JoinColumn(name = "calendarId")
    private ClassCalendar calendar;

    @Builder
    public AdminCoupon(Integer couponId, String couponType, String discountType, Integer discount, LocalDateTime validFrom, LocalDateTime validUntil, LocalDateTime createdAt, String couponName, ClassCalendar calendar) {
        this.couponId = couponId;
        this.couponType = couponType;
        this.discountType = discountType;
        this.discount = discount;
        this.validFrom = validFrom;
        this.validUntil = validUntil;
        this.createdAt = createdAt;
        this.couponName = couponName;
        this.calendar = calendar;
    }

    // 엔티티 -> toDto
    public AdminCoupon toEntity() {
        return new AdminCoupon(
                this.couponId,
                this.couponType,
                this.discountType,
                this.discount,
                this.validFrom,
                this.validUntil,
                this.createdAt,
                this.couponName,
                this.calendar
        );
    }
}


