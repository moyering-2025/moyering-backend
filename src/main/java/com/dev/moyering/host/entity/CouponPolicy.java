package com.dev.moyering.host.entity;

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
//안쓰는것..? 아마도?
@Entity
@Table(name = "coupon_policy")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponPolicy {

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer policyId;

    @Column(nullable = false)
    private String couponType; // 비율이면 'RT', 금액할인이면 'AMT'

    @Column(nullable = false)
    private Integer discount; // 예: 10 (%), 5000 (금액)

    @Column(nullable = false)
    private Boolean active;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}