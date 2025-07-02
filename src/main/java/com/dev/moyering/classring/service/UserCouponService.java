package com.dev.moyering.classring.service;

import java.util.List;

import com.dev.moyering.classring.dto.UserCouponDto;
import com.dev.moyering.common.dto.PageResponseDto;

public interface UserCouponService {
	void downloadClassCoupon(Integer userId, Integer classCouponId) throws Exception;
	List<UserCouponDto> getByUserIdAndClassId(Integer userId, Integer classId) throws Exception;
	PageResponseDto<UserCouponDto> getUserCoponByUserId(Integer userId, int page, int size ) throws Exception;
}
