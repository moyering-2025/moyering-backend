package com.dev.moyering.admin.dto;

import lombok.Builder;
import lombok.Getter;

import java.sql.Date;

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
}
