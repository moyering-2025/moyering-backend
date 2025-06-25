package com.dev.moyering.host.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.dev.moyering.host.dto.ClassCouponDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "class_coupon")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassCoupon {

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer classCouponId;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private HostClass hostClass;

    @Column(nullable = false)
    private String couponName; //쿠폰 이름
    @Column
    private Integer amount; //발급매수
    @Column
    private LocalDateTime validFrom; //쿠폰 시작일
    @Column
    private LocalDateTime validUntil; //쿠폰 종료일
    @Column(nullable = false)
    private Integer usedCnt; // 사용량
    @Column(nullable = false)
    private String discountType; //할인 유형 =>비율할인 : RT, 정액할인 ; AMT
    @Column(nullable = false)
    private Integer discount; //할인 가격 또는 비율
    @Column(nullable = false)
    private String status; //활성, 만료, 소진
    
    public ClassCouponDto toDto() {
    	ClassCouponDto dto = ClassCouponDto.builder()
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
    	if (hostClass!= null) {
    		dto.setClassId(hostClass.getClassId());
    		dto.setClassName(hostClass.getName());
    	}
    	return dto;
    }
}
