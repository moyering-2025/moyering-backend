package com.dev.moyering.user.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.dev.moyering.admin.entity.AdminCoupon;
import com.dev.moyering.host.entity.ClassCoupon;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_coupon")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCoupon {

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer ucId;

    @Column(nullable = false)
    private String status; // '사용', '미사용', '만료'

    @Column(nullable = false)
    private LocalDateTime downloadedAt;

    private LocalDateTime usedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "class_coupon_id")
    private ClassCoupon classCoupon;

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private AdminCoupon adminCoupon;
}
