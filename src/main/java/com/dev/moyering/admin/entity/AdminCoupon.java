package com.dev.moyering.admin.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "admin_coupon")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminCoupon {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer couponId;

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
}
