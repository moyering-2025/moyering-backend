package com.dev.moyering.user.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.dev.moyering.admin.entity.AdminBadge;
import com.dev.moyering.admin.service.AdminBadgeService;
import com.dev.moyering.user.entity.User;
import com.dev.moyering.user.entity.UserBadge;
import com.dev.moyering.user.repository.UserBadgeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserBadgeServiceImpl implements UserBadgeService {
	private final UserBadgeRepository badgeRepository;
    private final AdminBadgeService adminBadgeService;

	@Override
	public void giveBadge(Integer userId, Integer badgeId) throws Exception {
		boolean exists = badgeRepository.existsByUserUserIdAndBadgeBadgeId(userId, badgeId);
		if (exists) return;

		AdminBadge badge = adminBadgeService.getBadgeById(badgeId);

        UserBadge ub = UserBadge.builder()
        		.user(User.builder().userId(userId).build())
        		.badge(badge)
        		.earnedAt(Timestamp.valueOf(LocalDateTime.now()))
        		.isRepresentative(badgeId.equals(1))
        		.badge_img(badge.getBadgeImg())
        		.build();
        badgeRepository.save(ub);
	}

}
