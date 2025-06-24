package com.dev.moyering.admin.service;

import com.dev.moyering.admin.dto.AdminCouponDto;
import com.dev.moyering.admin.dto.AdminNoticeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminCouponService {
    Page<AdminNoticeDto> getCouponList(String searchKeyword, Pageable pageable) throws Exception; // 리스트 조회

    AdminCouponDto getCouponById(Integer couponId) throws Exception; // 단건 조회 (모달)

    AdminCouponDto createCoupon(AdminCouponDto couponDto) throws Exception;     // 등록 + 검증 + 변환

    AdminCouponDto updateCoupon(Integer couponId, AdminCouponDto dto) throws Exception; // 수정 + 검증 + 권한체크

    void deleteCoupon(Integer couponId) throws Exception;             // 삭제 + 권한체크 + 로깅

}