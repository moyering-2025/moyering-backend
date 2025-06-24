package com.dev.moyering.admin.dto;

import lombok.Builder;
import lombok.Getter;

import java.sql.Date;

@Builder
@Getter
public class AdminCouponSearchCond {
    private final String keyword;  // 검색어
    private final Date fromDate; // 쿠폰 시작일자
    private final Date toDate; // 쿠폰 만료일자
    private final String status; // 쿠폰 상태 필터(활성, 만료, 소진)
    private final String couponType; // 쿠폰 주체자 (전체, 관리자, 호스트)

    public AdminCouponSearchCond(String keyword,  Date fromDate, Date toDate, String status, String couponType) {
        this.keyword = keyword;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.status = status;
        this.couponType = couponType;
    }
}
