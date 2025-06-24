package com.dev.moyering.admin.repository;

import com.dev.moyering.admin.dto.AdminCouponDto;
import com.dev.moyering.admin.dto.AdminCouponSearchCond;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminCouponRepositoryCustom {
    // 목록 조회 및 검색
    Page<AdminCouponDto> findCouponByCond(AdminCouponSearchCond cond, Pageable pageable);
//    AdminCouponDto findCouponByCouponId(Integer couponId);
}