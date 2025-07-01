package com.dev.moyering.socialing.repository;

import com.dev.moyering.socialing.dto.FeedDto;

import java.util.List;

public interface FeedRepositoryCustom {

    // 댓글, 조회수 함께 조회
    List<FeedDto> findAllWithCounts();

    // 전체피드 조회
    List<FeedDto> findFeeds(String sortType, Integer userId); //userid는 팔로우용


    // 작성자(userId)의 최신 3개 피드 img1만 조회
    List<String> findTop3Img1ByUserId(Integer userId);

    List<FeedDto> findFeedsWithoutLiked();

    //인기피드
    List<FeedDto> findTopLikedFeeds(int offset, int size);

    // 좋아요여부 없이 조회
    List<FeedDto> findFeedsWithoutLiked(String sortType);

}
