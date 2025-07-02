package com.dev.moyering.user.repository;

import com.dev.moyering.admin.dto.*;
import com.dev.moyering.user.dto.UserPaymentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserPaymentRepositoryCustom {
    // 관리자페이지 사용자 결제내역 조회 (검색(주문번호, 결제자 ID, 클래스명) + 필터 +  페이지 기반)
    Page <AdminPaymentDto> searchPaymentList(AdminPaymentSearchCond cond, Pageable pageable);

    // 검색 후 클래스목록 건수
    Long countPaymentList(AdminPaymentSearchCond cond);

}
