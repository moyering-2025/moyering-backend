package com.dev.moyering.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dev.moyering.admin.dto.AdminPaymentDto;
import com.dev.moyering.admin.dto.AdminPaymentSearchCond;
import com.dev.moyering.classring.dto.UserPaymentHistoryDto;
import com.dev.moyering.classring.dto.UtilSearchDto;


public interface UserPaymentRepositoryCustom {
    // 관리자페이지 사용자 결제내역 조회 (검색(주문번호, 결제자 ID, 클래스명) + 필터 +  페이지 기반)
    Page <AdminPaymentDto> searchPaymentList(AdminPaymentSearchCond cond, Pageable pageable);

    // 검색 후 클래스목록 건수
    Long countPaymentList(AdminPaymentSearchCond cond);

    //마이페이지 수강 결제 내역
    Page<UserPaymentHistoryDto> findUserPaymentHistory(UtilSearchDto dto, Pageable pageable) throws Exception;
}
