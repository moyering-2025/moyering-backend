package com.dev.moyering.admin.service;

import org.springframework.stereotype.Service;

import com.dev.moyering.admin.dto.AdminBadgeDto;
import com.dev.moyering.admin.entity.AdminBadge;
import com.dev.moyering.admin.repository.AdminBadgeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminBadgeServiceImpl implements AdminBadgeService {
	private final AdminBadgeRepository adminBadgeRepository;

	@Override
	public AdminBadge getBadgeById(Integer badgeId) throws Exception {
		return adminBadgeRepository.findById(badgeId)
				.orElseThrow(()-> new Exception("해당 뱃지가 없습니다."));
	}
}
