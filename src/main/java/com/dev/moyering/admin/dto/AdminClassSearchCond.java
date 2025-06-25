package com.dev.moyering.admin.dto;

import lombok.Builder;
import lombok.Getter;
import java.sql.Date;

@Builder
@Getter
public class AdminClassSearchCond {
    private final String keyword; // 키워드(강사 아이디, 강사명, 클래스명)
    private final String firstCategory; // 1차 카테고리
    private final String secondCategory; // 2차 카테고리
    private final Date fromDate; // 시작일
    private final Date toDate; // 종료일
    private final String statusFilter; // 상태 필터 (승인, 대기, 거절)

    public AdminClassSearchCond(String keyword, String firstCategory, String secondCategory, Date fromDate, Date toDate, String statusFilter) {
        this.keyword = keyword;
        this.firstCategory = firstCategory;
        this.secondCategory = secondCategory;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.statusFilter = statusFilter;
    }
}
