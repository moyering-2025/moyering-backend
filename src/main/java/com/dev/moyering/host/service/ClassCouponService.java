package com.dev.moyering.host.service;

import java.util.List;

import com.dev.moyering.host.dto.ClassCouponDto;
import com.dev.moyering.host.entity.ClassCoupon;

public interface ClassCouponService {
	List<ClassCouponDto> getCouponByClassId(Integer classId) throws Exception;
	void downloadClassCoupon(Integer userId, Integer classCouponId) throws Exception;
	void insertHostSelectedCoupon (List<ClassCouponDto> couponList,Integer classId)throws Exception;
}
