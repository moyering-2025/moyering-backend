package com.dev.moyering.user.service;


public interface UserBadgeService {
	// 점수와 무관한 배지 수여
	void giveBadge(Integer userId, Integer badgeId) throws Exception;
	//점수 부여 후 배지 수여
	void giveBadgeWithScore(Integer userId) throws Exception;
}
