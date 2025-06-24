package com.dev.moyering.admin.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public enum CouponStatus {
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

        // 현재 시간이 유효기간을 벗어난 경우
        if (validFrom != null && now.isBefore(validFrom)) {
            return EXPIRED; // 아직 시작 전
        }
        if (validUntil != null && now.isAfter(validUntil)) {
            return EXPIRED; // 이미 종료
        }

        // 유효기간 내이지만 발급 가능 수량을 모두 발급한 경우
        if (actualIssuedCount != null && issueCount != null && actualIssuedCount >= issueCount) {
            return EXHAUSTED;
        }

        // 유효기간 내이고 발급 가능한 경우
        return ACTIVE;
    }
}