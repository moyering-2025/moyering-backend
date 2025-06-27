package com.dev.moyering.admin.dto;

import com.dev.moyering.admin.entity.AdminCoupon;
import com.dev.moyering.admin.entity.CouponStatus;
import com.dev.moyering.classring.entity.UserCoupon;
import com.dev.moyering.host.entity.HostClass;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor // 매개변수 없는 기본 생성자 생성
@AllArgsConstructor // 모든 필드를 매개변수로 받는 생성자를 생성
public class AdminCouponDto {
    private Integer couponId; // 쿠폰 아이디
    private String couponType; // 쿠폰 유형 (관리자면 'MG' 강사면 'HT")
    private String couponCode; // 쿠폰 코드
    private String discountType;  // 비율이면 'RT', 금액할인이면 'AMT'
    private Integer discount; // 예: 10 (%), 5000 (금액)
    private Integer issueCount; // 발급매수
    private LocalDateTime validFrom; //쿠폰 시작일
    private LocalDateTime validUntil; //쿠폰 종료일
    private LocalDateTime createdAt; // 쿠폰 생성일

    // 동적으로 계산되는 필드
    private CouponStatus status; // 쿠폰 상태

    public AdminCouponDto(Integer couponId, String couponType, String couponCode,
                          String discountType, Integer discount, Integer issueCount,
                          LocalDateTime validFrom, LocalDateTime validUntil,
                          LocalDateTime createdAt) {
        this.couponId = couponId;
        this.couponType = couponType;
        this.couponCode = couponCode;
        this.discountType = discountType;
        this.discount = discount;
        this.issueCount = issueCount;
        this.validFrom = validFrom;
        this.validUntil = validUntil;
        this.createdAt = createdAt;
        // status는 null로 초기화 (나중에 설정)
    }


    // DTO -> Entity 변환
    public AdminCoupon toEntity() {
        return AdminCoupon.builder()
                .couponId(this.couponId)
                .couponType(this.couponType)
                .couponCode(this.couponCode)
                .discountType(this.discountType)
                .discount(this.discount)
                .issueCount(this.issueCount)
                .validFrom(this.validFrom)
                .validUntil(this.validUntil)
                .createdAt(this.createdAt)
                .build();
    }
}
