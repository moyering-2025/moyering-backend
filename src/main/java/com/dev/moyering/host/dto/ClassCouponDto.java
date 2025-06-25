package com.dev.moyering.host.dto;

import java.time.LocalDateTime;

import com.dev.moyering.host.entity.ClassCoupon;
import com.dev.moyering.host.entity.HostClass;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ClassCouponDto {
    private Integer classCouponId;
    private Integer classId;
    private String couponName;
    private Integer amount;
    private LocalDateTime validFrom; //쿠폰 시작일
    private LocalDateTime validUntil; //쿠폰 종료일
    private Integer usedCnt; // 사용량
    private String discountType; //할인 유형 =>비율할인 : RT, 정액할인 ; AMT
    private Integer discount; //할인 가격 또는 비율
    private String status; //활성, 만료, 소진

    //부가정보
    private String className;
    
    public ClassCoupon toEntity() {
    	ClassCoupon entity = ClassCoupon.builder()
    			.classCouponId(classCouponId)
    			.couponName(couponName)
    			.amount(amount)
    			.validFrom(validFrom)
    			.validUntil(validUntil)
    			.usedCnt(usedCnt)
    			.discountType(discountType)
    			.discount(discount)
    			.status(status)
    			.build();
    	if (classId!= null) {
    		entity.setHostClass(HostClass.builder()
    				.classId(classId)
    				.build());
    	}
    	return entity;
    }
}
