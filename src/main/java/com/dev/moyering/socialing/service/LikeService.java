package com.dev.moyering.socialing.service;

import com.dev.moyering.socialing.dto.FeedDto;

import java.util.List;

public interface LikeService {
    void toggleLike(Integer feedId, Integer userId) throws Exception;
    List<FeedDto> getFeedsWithLikeStatus(Integer userId)throws Exception;
}
