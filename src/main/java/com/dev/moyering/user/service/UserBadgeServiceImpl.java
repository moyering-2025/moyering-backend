package com.dev.moyering.user.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dev.moyering.admin.entity.AdminBadge;
import com.dev.moyering.admin.service.AdminBadgeService;
import com.dev.moyering.user.entity.User;
import com.dev.moyering.user.entity.UserBadge;
import com.dev.moyering.user.repository.UserBadgeRepository;
import com.dev.moyering.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserBadgeServiceImpl implements UserBadgeService {
	private final UserBadgeRepository badgeRepository;
    private final AdminBadgeService adminBadgeService;
    private final UserRepository userRepository;

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

	@Transactional
	@Override
	public void giveBadgeWithScore(Integer userId) throws Exception {
		List<UserBadge> userbadges = badgeRepository.findByUser_UserId(userId);
		Set<Integer> ubIds = userbadges.stream().map(ub-> ub.getBadge().getBadgeId())
				.collect(Collectors.toSet());
		
		User user = userRepository.findById(userId).orElseThrow(()-> new Exception("해당 유저가 존재하지 않습니다"));
		Integer userScore = user.getActiveScore();
		List<AdminBadge> badges = adminBadgeService.getScoreBadges();
		
		for (AdminBadge ab : badges) {
			if (userScore>=ab.getCumulScore() && !ubIds.contains(ab.getBadgeId())) {
		        UserBadge ub = UserBadge.builder()
		        		.user(user)
		        		.badge(ab)
		        		.earnedAt(Timestamp.valueOf(LocalDateTime.now()))
		        		.isRepresentative(false)
		        		.badge_img(ab.getBadgeImg())
		        		.build();
		        badgeRepository.save(ub);
			}
		}
	}

}
