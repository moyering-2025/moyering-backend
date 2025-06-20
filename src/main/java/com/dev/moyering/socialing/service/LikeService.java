package com.dev.moyering.socialing.service;

public interface LikeService {
    void toggleLike(Integer feedId, Integer userId) throws Exception;
}
