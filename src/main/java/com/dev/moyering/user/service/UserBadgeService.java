package com.dev.moyering.user.service;

import com.dev.moyering.user.entity.User;

public interface UserBadgeService {
	void giveBadge(Integer userId, Integer badgeId) throws Exception;

}
