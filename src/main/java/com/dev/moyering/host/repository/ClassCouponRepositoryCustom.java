package com.dev.moyering.host.repository;

import java.util.List;

import com.dev.moyering.host.entity.ClassCoupon;

public interface ClassCouponRepositoryCustom {
	List<ClassCoupon> findAllByClassId(Integer classId) throws Exception;
}
