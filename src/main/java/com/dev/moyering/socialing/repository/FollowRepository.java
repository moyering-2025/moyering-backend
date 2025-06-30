package com.dev.moyering.socialing.repository;

import com.dev.moyering.socialing.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Integer>, FollowRepositoryCustom {

    Optional<Follow> findByFollowerUserIdAndFollowingUserId(Integer followerId, Integer followingId);

    List<Follow> findAllByFollowerUserId(Integer followerId);

    void deleteByFollowerUserIdAndFollowingUserId(Integer followerId, Integer followingId);
}
