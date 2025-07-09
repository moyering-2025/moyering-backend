package com.dev.moyering.admin.service;

import java.util.List;

import org.springframework.stereotype.Service;

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

	@Override
	public List<AdminBadge> getScoreBadges() throws Exception {
		return adminBadgeRepository.findByCumulScoreGreaterThan(0);
	}
}
