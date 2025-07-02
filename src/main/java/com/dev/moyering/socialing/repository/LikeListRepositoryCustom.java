package com.dev.moyering.socialing.repository;

import com.dev.moyering.socialing.dto.FeedDto;

import java.util.List;

public interface LikeListRepositoryCustom {

    // 좋아요한 피드 id 리스트
    List<Integer> findFeedIdsByUserId(Integer userId);

    List<FeedDto> findAllWithLikedByUser(Integer userId);

}
