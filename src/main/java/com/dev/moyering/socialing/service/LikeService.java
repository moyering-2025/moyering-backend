package com.dev.moyering.socialing.service;

public interface LikeService {
    void toggleLike(Integer feedId, String username) throws Exception;
}
