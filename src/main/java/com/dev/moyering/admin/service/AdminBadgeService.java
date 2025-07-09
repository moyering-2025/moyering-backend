package com.dev.moyering.admin.service;

import java.util.List;

import com.dev.moyering.admin.entity.AdminBadge;

public interface AdminBadgeService {
	AdminBadge getBadgeById(Integer badgeId) throws Exception;
	List<AdminBadge> getScoreBadges() throws Exception;
}
