package com.dev.moyering.admin.service;

import com.dev.moyering.admin.dto.AdminCouponDto;
import com.dev.moyering.admin.dto.AdminNoticeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class AdminCouponServiceImpl implements AdminCouponService{
    @Override
    public Page<AdminNoticeDto> getCouponList(String searchKeyword, Pageable pageable) throws Exception {
        return null;
    }

    @Override
    public AdminCouponDto getCouponById(Integer couponId) throws Exception {
        return null;
    }

    @Override
    public AdminCouponDto createCoupon(AdminCouponDto couponDto) throws Exception {
        return null;
    }

    @Override
    public AdminCouponDto updateCoupon(Integer couponId, AdminCouponDto dto) throws Exception {
        return null;
    }

    @Override
    public void deleteCoupon(Integer couponId) throws Exception {

    }
}
