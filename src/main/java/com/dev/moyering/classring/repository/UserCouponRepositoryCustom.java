package com.dev.moyering.classring.repository;

import java.util.List;

import com.dev.moyering.classring.entity.UserCoupon;

public interface UserCouponRepositoryCustom {
	List<UserCoupon> findAvailableCoupons(Integer userId, Integer classId) throws Exception;
}
