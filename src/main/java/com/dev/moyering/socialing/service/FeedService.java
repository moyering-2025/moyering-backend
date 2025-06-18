package com.dev.moyering.socialing.service;

import com.dev.moyering.socialing.dto.FeedDto;

import java.util.List;

public interface FeedService {
    List<FeedDto> getFeeds(String sortType, String userId) throws Exception;

//    FeedDto getFeedDetail(Integer feedId, Integer userId) throws Exception;
}
