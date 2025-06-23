package com.dev.moyering.admin.dto;

import com.dev.moyering.admin.entity.AdminCoupon;
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
    private String discountType;  // 비율이면 'RT', 금액할인이면 'AMT'
    private Integer discount; // 예: 10 (%), 5000 (금액)
    private Integer issueCount; // 발급매수
    private LocalDateTime validFrom; //쿠폰 시작일
    private LocalDateTime validUntil; //쿠폰 종료일
    private LocalDateTime createdAt; // 쿠폰 생성일
    private String couponName; // 쿠폰 이름
    private ClassCalendar calendar; // 일정 아이디



    // DTO -> Entity 변환 (  * Repository에서 사용하는 생성자로 순서가 Repository 구현과 정확히 일치해야 함)
    public AdminCoupon toEntity() {
        return AdminCoupon.builder()
                .couponId(this.couponId)
                .couponType(this.couponType)
                .discountType(this.discountType)
                .discount(this.discount)
                .validFrom(this.validFrom)
                .validUntil(this.validUntil)
                .createdAt(this.createdAt)
                .couponName(this.couponName)
                .calendar(this.calendar)
                .build();
    }
}
