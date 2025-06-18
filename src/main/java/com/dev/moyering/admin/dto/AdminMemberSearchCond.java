package com.dev.moyering.admin.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Builder
public class AdminMemberSearchCond {
    private final String keyword; // 키워드 검색

    private final String userType; // 사용자 구분 필터

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private final LocalDate from; // 가입기간 FROM

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private final LocalDate to; // 가입기간 TO

    public AdminMemberSearchCond(String keyword, String userType, LocalDate from, LocalDate to) {
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
