package com.dev.moyering.socialing.repository;

import com.dev.moyering.socialing.entity.LikeList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeListRepository extends JpaRepository<LikeList, Integer> {

    // 로그인 유저가 해당 피드에 좋아요 눌렀는지 확인
    boolean existsByFeedFeedIdAndUserUserId(Integer feedId, Integer userId);

    // 피드의 전체 좋아요 수
    Long countByFeedFeedId(Integer feedId);

    // 좋아요 상세 엔티티 조회 (삭제용)
    Optional<LikeList> findByFeed_FeedIdAndUser_Username(Integer feedId, String id);

    // 좋아요 삭제
    void deleteByFeed_FeedIdAndUser_Username(Integer feedId, Integer id);
}
