package com.dev.moyering.socialing.repository;

import com.dev.moyering.socialing.dto.FeedDto;

import java.util.List;

public interface FeedRepositoryCustom {

    //댓글, 조회수 함께 조회
    List<FeedDto> findAllWithCounts();

    //전체피드 조회
    List<FeedDto> findFeeds(String sortType, String userId); //userid는 팔로우용
}
