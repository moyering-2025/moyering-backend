package com.dev.moyering.admin.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Builder
public class AdminMemberSearchCond {
    private final String keyword; // 키워드 검색

    private final String userType; // 사용자 구분 필터
    private final Date from;
    private final Date to;

    public AdminMemberSearchCond(String keyword, String userType, Date from, Date to) {
        this.keyword = keyword;
        this.userType = userType;
        this.from = from;
        this.to = to;
    }

    public AdminMemberSearchCond() {
        this.keyword = null;
        this.userType = null;
        this.from = null;
        this.to = null;
    }
}
