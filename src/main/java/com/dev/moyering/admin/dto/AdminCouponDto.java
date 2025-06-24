package com.dev.moyering.admin.dto;

import com.dev.moyering.admin.entity.AdminCoupon;
import com.dev.moyering.admin.entity.CouponStatus;
import com.dev.moyering.host.entity.ClassCalendar;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    private String couponName; // 쿠폰 이름
    private ClassCalendar calendar; // 일정 아이디

    // 동적으로 계산되는 필드
    private CouponStatus status; // 쿠폰 상태
    private Integer actualIssuedCount; // 실제 발급된 쿠폰 수 (user_coupon 테이블 기준)
    private Integer usedCount; // 사용된 쿠폰 수 (user_coupon.status='사용')
    private Integer remainingCount; // 남은 발급 가능 쿠폰 수




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
                .couponName(this.couponName)
                .calendar(this.calendar)
                .build();
    }

    // 쿠폰 상태 계산 메서드
    public CouponStatus calculateStatus() {
        return CouponStatus.determineCouponStatus(
                this.validFrom,
                this.validUntil,
                this.issueCount,
                this.actualIssuedCount,
                this.usedCount
        );
    }

    // 남은 발급 가능 쿠폰 수 계산
    public Integer calculateRemainingCount() {
        if (this.issueCount == null) return 0;
        if (this.actualIssuedCount == null) return this.issueCount;
        return Math.max(0, this.issueCount - this.actualIssuedCount);
    }
}