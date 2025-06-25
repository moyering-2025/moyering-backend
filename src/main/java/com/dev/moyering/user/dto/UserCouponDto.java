package com.dev.moyering.user.dto;

import java.time.LocalDateTime;

import com.dev.moyering.admin.entity.AdminCoupon;
import com.dev.moyering.host.entity.ClassCoupon;
import com.dev.moyering.host.entity.HostClass;
import com.dev.moyering.user.entity.User;
import com.dev.moyering.user.entity.UserCoupon;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCouponDto {
    private Integer ucId;
    private String status; // '사용', '미사용', '만료'
    private LocalDateTime downloadedAt;
    private LocalDateTime usedAt;
    private Integer userId;
    private Integer classCouponId;
    private Integer couponId;
    
    //부가 정보
    private String couponName;
    private String className;
    
    public UserCoupon toEntity() {
    	UserCoupon entity = UserCoupon.builder()
    			.ucId(ucId)
    			.status(status)
    			.downloadedAt(downloadedAt)
    			.usedAt(usedAt)
    			.build();
    	if(userId!=null) {
    		entity.setUser(User.builder()
    				.userId(userId)
    				.build());
    	}
    	if(classCouponId!=null) {
    		entity.setClassCoupon(ClassCoupon.builder()
    				.classCouponId(classCouponId)
    				.build());
    	}
    	if(couponId!=null) {
    		entity.setAdminCoupon(AdminCoupon.builder()
    				.couponId(couponId)
    				.build());
    	}
    	return entity;
    }
}
