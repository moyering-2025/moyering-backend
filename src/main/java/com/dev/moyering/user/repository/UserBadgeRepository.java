package com.dev.moyering.user.repository;

import com.dev.moyering.user.dto.UserBadgeDto;
import com.dev.moyering.user.entity.UserBadge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserBadgeRepository extends JpaRepository<UserBadge, Integer> {

    // 유저가 가진 배지 리스트
    List<UserBadge> findByUser_UserId(Integer userId);

    // 유저의 대표 배지
    Optional<UserBadge> findByUser_UserIdAndIsRepresentativeTrue(Integer userId);
    //유저가 뱃지를 가지고 있는지 체크
	boolean existsByUserUserIdAndBadgeBadgeId(Integer userId, Integer badgeId);

}
