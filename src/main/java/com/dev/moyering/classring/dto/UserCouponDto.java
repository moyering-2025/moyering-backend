package com.dev.moyering.classring.dto;

import java.time.LocalDateTime;

import com.dev.moyering.admin.entity.AdminCoupon;
import com.dev.moyering.classring.entity.UserCoupon;
import com.dev.moyering.host.entity.ClassCoupon;
import com.dev.moyering.host.entity.HostClass;
import com.dev.moyering.user.entity.User;

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
    private LocalDateTime validFrom;
    private LocalDateTime validUntil;
    private String discountType;
    private Integer discount; // 예: 10 (%), 5000 (금액)
    
    public UserCoupon toEntity() {
	    return UserCoupon.builder()
	        .ucId(ucId)
	        .status(status)
	        .downloadedAt(downloadedAt)
	        .usedAt(usedAt)
	        .user(userId != null ? User.builder().userId(userId).build() : null)
	        .classCoupon(classCouponId != null ? ClassCoupon.builder().classCouponId(classCouponId).build() : null)
	        .adminCoupon(couponId != null ? AdminCoupon.builder().couponId(couponId).build() : null)
	        .build();
    }
}
