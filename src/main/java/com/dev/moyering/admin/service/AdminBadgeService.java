package com.dev.moyering.admin.service;

import com.dev.moyering.admin.dto.AdminBadgeDto;
import com.dev.moyering.admin.entity.AdminBadge;

public interface AdminBadgeService {
	AdminBadge getBadgeById(Integer badgeId) throws Exception;
}
