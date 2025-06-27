package com.dev.moyering.host.service;

import java.util.List;

import com.dev.moyering.host.dto.ClassCouponDto;

public interface ClassCouponService {
	List<ClassCouponDto> getCouponByClassId(Integer classId) throws Exception;
}
