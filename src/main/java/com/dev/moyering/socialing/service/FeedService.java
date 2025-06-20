package com.dev.moyering.socialing.service;

import com.dev.moyering.socialing.dto.CommentDto;
import com.dev.moyering.socialing.dto.FeedDto;

import java.util.List;

public interface FeedService {
    List<FeedDto> getFeeds(String sortType, String userId) throws Exception;

    FeedDto getFeedById(Integer feedId) throws Exception;

    List<CommentDto> getCommentsByFeedId(Integer feedId) throws Exception;

    Long getLikeCountByFeedId(Integer feedId) throws Exception;

    boolean checkUserLikeFeed(Integer feedId,String userId) throws Exception;

    FeedDto getFeedDetail(Integer feedId, Integer currentUserId);

}
