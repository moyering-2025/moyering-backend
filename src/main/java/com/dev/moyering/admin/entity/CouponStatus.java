package com.dev.moyering.admin.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public enum CouponStatus {
    SCHEDULED("예정"),
    ACTIVE("활성"),
    EXPIRED("만료"),
    EXHAUSTED("소진");

    private final String description;

    // 쿠폰 상태 판별 로직
    public static CouponStatus determineCouponStatus(LocalDateTime validFrom,
                                                     LocalDateTime validUntil,
                                                     Integer issueCount,
                                                     Integer actualIssuedCount,
                                                     Integer usedCount) {
        LocalDateTime now = LocalDateTime.now();

        // 쿠폰 유효기간에셔 쿠폰 시작일이 오늘 이전일 경우 '예정'
        if (validFrom != null && now.isBefore(validFrom) ) {
            return SCHEDULED; // 예정
        }

        // 쿠폰 유효기간에서 쿠폰 종료일이 오늘 이전일 경우 '만료'
        if (validUntil != null && now.isAfter(validUntil)) {
            return EXPIRED; //  종료
        }

        // 유효기간 내이지만 발급 가능 수량을 모두 발급한 경우
        if (actualIssuedCount != null && issueCount != null && actualIssuedCount >= issueCount) {
            return EXHAUSTED; // 소진
        }

        // 유효기간 내이고 발급 가능한 경우
        // - validFrom == null 이거나 now >= validFrom (시작됨)
        // - validUntil == null 이거나 now <= validUntil (아직 끝나지 않음)
        return ACTIVE;
    }
}