package com.dev.moyering.classring.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dev.moyering.classring.entity.UserCoupon;

public interface UserCouponRepositoryCustom {
	List<UserCoupon> findAvailableCoupons(Integer userId, Integer classId) throws Exception;
	Page<UserCoupon> findAllCouponsByUserId(Integer userId, Pageable pageable) throws Exception;

}
