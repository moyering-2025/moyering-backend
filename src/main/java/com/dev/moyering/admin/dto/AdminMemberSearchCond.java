package com.dev.moyering.admin.dto;


import lombok.Builder;
import lombok.Getter;
import java.util.Date;

@Builder
@Getter
public class AdminMemberSearchCond {
    private final String keyword;
    private final String userType;
    private final Date fromDate;
    private final Date toDate;

    public AdminMemberSearchCond(String keyword, String userType, Date fromDate, Date toDate) {
        this.keyword = keyword;
        this.userType = userType;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }
}