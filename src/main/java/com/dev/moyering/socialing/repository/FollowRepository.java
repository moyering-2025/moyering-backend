package com.dev.moyering.socialing.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dev.moyering.socialing.entity.Follow;
import com.dev.moyering.user.entity.User;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Integer>, FollowRepositoryCustom {

	Optional<Follow> findByFollowerUserIdAndFollowingUserId(Integer followerId, Integer followingId);

	List<Follow> findAllByFollowerUserId(Integer followerId);

	void deleteByFollowerUserIdAndFollowingUserId(Integer followerId, Integer followingId);

	Integer countByFollower(User user);    // 내가 팔로우한 사람 수
	Integer countByFollowing(User user);   // 나를 팔로우한 사람 수
}
