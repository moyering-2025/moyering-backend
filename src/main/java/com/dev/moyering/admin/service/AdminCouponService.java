package com.dev.moyering.admin.service;

import java.util.List;

import com.dev.moyering.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dev.moyering.admin.dto.AdminCouponDto;
import com.dev.moyering.admin.dto.AdminCouponSearchCond;

public interface AdminCouponService {
    Page<AdminCouponDto> getCouponList(AdminCouponSearchCond cond, Pageable pageable) throws Exception; // 리스트 조회

    AdminCouponDto getCouponById(Integer couponId) throws Exception; // 단건 조회 (모달)

    AdminCouponDto createCoupon(AdminCouponDto couponDto) throws Exception;     // 등록 + 검증 + 변환

    AdminCouponDto updateCoupon(Integer couponId, AdminCouponDto dto) throws Exception; // 수정 + 검증 + 권한체크

    AdminCouponDto deleteCoupon(Integer couponId) throws Exception;             // 삭제 + 권한체크 + 로깅
    
    List<AdminCouponDto> selectHostAllCoupon(String type) throws Exception; //모든 쿠폰 조회

    //    // 쿠폰 지급을 위한 활성사용자 조회
    List<User> distributeCouponToActiveUsers(Integer couponId);


}