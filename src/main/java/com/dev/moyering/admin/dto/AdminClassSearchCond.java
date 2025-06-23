package com.dev.moyering.admin.dto;

import lombok.Builder;
import lombok.Getter;
import java.sql.Date;

@Builder
@Getter
public class AdminClassSearchCond {
    private final String keyword;
    private final String category; // 카테고리 필터
    private final Date fromDate;
    private final Date toDate;
    private final String statusFilter; // 상태 필터

    public AdminClassSearchCond(String keyword, String category, Date fromDate, Date toDate, String statusFilter) {
        this.keyword = keyword;
        this.category = category;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.statusFilter = statusFilter;
    }
}
