package com.dev.moyering.host.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "class_coupon")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassCoupon {

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer classCouponId;

    @ManyToOne
    @JoinColumn(name = "policy_id")
    private CouponPolicy policy;

    @ManyToOne
    @JoinColumn(name = "calendar_id")
    private ClassCalendar calendar;

    @Column
    private String couponName; //쿠폰 이름
    @Column
    private String amount; //발급매수
}
