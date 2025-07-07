package com.dev.moyering.socialing.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dev.moyering.common.dto.MainSearchRequestDto;
import com.dev.moyering.socialing.dto.FeedDto;
import com.dev.moyering.socialing.entity.Feed;

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
    
    List<Feed> findSearchFeed(MainSearchRequestDto dto);

}
