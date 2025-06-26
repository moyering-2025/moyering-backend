package com.dev.moyering.host.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.dev.moyering.classring.entity.UserCoupon;
import com.dev.moyering.classring.repository.UserCouponRepository;
import com.dev.moyering.host.dto.ClassCouponDto;
import com.dev.moyering.host.entity.ClassCoupon;
import com.dev.moyering.host.repository.ClassCouponRepository;
import com.dev.moyering.user.entity.User;
import com.dev.moyering.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClassCouponServiceImpl implements ClassCouponService {
	private final ClassCouponRepository classCouponRepository;
	
	@Override
	public List<ClassCouponDto> getCouponByClassId(Integer classId) throws Exception  {
		return classCouponRepository.findAllByClassId(classId).stream()
				.map(c->c.toDto()).collect(Collectors.toList());
	}

}
