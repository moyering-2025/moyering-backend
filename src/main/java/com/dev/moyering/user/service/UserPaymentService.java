package com.dev.moyering.user.service;

import com.dev.moyering.admin.dto.AdminPaymentDto;
import com.dev.moyering.admin.dto.AdminPaymentSearchCond;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface UserPaymentService {
    // 관리자 > 결제목록 조회
    Page <AdminPaymentDto> getPaymentList(AdminPaymentSearchCond cond, Pageable pageable);
}
