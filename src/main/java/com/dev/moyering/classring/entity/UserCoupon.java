package com.dev.moyering.classring.entity;

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
import com.dev.moyering.classring.dto.UserCouponDto;
import com.dev.moyering.host.entity.ClassCoupon;
import com.dev.moyering.host.entity.HostClass;
import com.dev.moyering.user.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    
    @Column
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
    
    public UserCouponDto toDto() {
    	UserCouponDto dto = UserCouponDto.builder()
    			.ucId(ucId)
    			.status(status)
    			.downloadedAt(downloadedAt)
    			.usedAt(usedAt)
    			.build();
    	if (user!= null ) {
    		dto.setUserId(user.getUserId());
    	}
    	if (classCoupon!= null) {
    		dto.setClassCouponId(classCoupon.getClassCouponId());
    		dto.setCouponName(classCoupon.getCouponName());
    		dto.setClassName(classCoupon.getHostClass().getName());
    		dto.setValidFrom(classCoupon.getValidFrom());
    		dto.setValidUntil(classCoupon.getValidUntil());
    		dto.setDiscountType(classCoupon.getDiscountType());
    		dto.setDiscount(classCoupon.getDiscount());
    	}
    	if (adminCoupon!= null) {
    		dto.setCouponId(adminCoupon.getCouponId());
    		dto.setCouponName(adminCoupon.getCouponCode());
    		dto.setValidFrom(adminCoupon.getValidFrom());
    		dto.setValidUntil(adminCoupon.getValidUntil());
    		dto.setDiscountType(adminCoupon.getDiscountType());
    		dto.setDiscount(adminCoupon.getDiscount());
    	}
    	
    	return dto;
    }
}
