package com.dev.moyering.admin.dto;

import lombok.Builder;
import lombok.Getter;
import java.sql.Date;
import java.util.List;

@Builder
@Getter
public class AdminClassSearchCond {
    private final String keyword; // 키워드(강사 아이디, 강사명, 클래스명)
    private final Date fromDate; // 시작일
    private final Date toDate; // 종료일
    private List<String> statusFilter; // 상태 필터 (승인, 대기, 거절)

    public AdminClassSearchCond(String keyword, Date fromDate, Date toDate, List<String> statusFilter) {
        this.keyword = keyword;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.statusFilter = statusFilter;
    }
}
