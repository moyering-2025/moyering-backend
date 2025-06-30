package com.dev.moyering.socialing.repository;

import com.dev.moyering.socialing.entity.LikeList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeListRepository extends JpaRepository<LikeList, Integer> ,LikeListRepositoryCustom {

    // 로그인 유저가 해당 피드에 좋아요 눌렀는지 확인
    boolean existsByFeedFeedIdAndUserUserId(Integer feedId, Integer userId);

    // 피드의 전체 좋아요 수
    Long countByFeedFeedId(Integer feedId);

    // 좋아요 상세 엔티티 조회 (삭제용)
    Optional<LikeList> findByFeed_FeedIdAndUser_UserId(Integer feedId, Integer userId);

    // 좋아요 삭제
    void deleteByFeedFeedIdAndUserUserId(Integer feedId, Integer userId);

    List<LikeList> findByFeedFeedId(Integer feedId);

}
