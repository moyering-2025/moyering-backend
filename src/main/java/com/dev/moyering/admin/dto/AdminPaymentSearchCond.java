package com.dev.moyering.admin.dto;

import lombok.Builder;
import lombok.Getter;

import java.sql.Date;
import java.time.LocalDate;

@Builder
@Getter
public class AdminPaymentSearchCond {
    private final String keyword; // 검색키워드 (주문번호, 결제 ID, 클래스명)
    private final Date fromDate; // 결제 기간 중 시작일
    private final Date toDate; // 결제 기간 중 종료일
    private final String status; // 결제상태 (전체, 결제완료, 취소)

    public AdminPaymentSearchCond(String keyword, Date fromDate, Date toDate, String status) {
        this.keyword = keyword;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.status = status;
    }

    // 안전한 LocalDate 변환 메서드들
    public LocalDate getFromLocalDate() {
        return fromDate != null ? fromDate.toLocalDate() : null;
    }

    public LocalDate getToLocalDate() {
        return toDate != null ? toDate.toLocalDate() : null;
    }

    // 날짜 범위 검색 여부 확인
    public boolean hasDateRange() {
        return fromDate != null || toDate != null;
    }

    // 시작 날짜만 있는지 확인
    public boolean hasFromDateOnly() {
        return fromDate != null && toDate == null;
    }

    // 종료 날짜만 있는지 확인
    public boolean hasToDateOnly() {
        return fromDate == null && toDate != null;
    }

    // 시작~종료 날짜 모두 있는지 확인
    public boolean hasFullDateRange() {
        return fromDate != null && toDate != null;
    }
}

