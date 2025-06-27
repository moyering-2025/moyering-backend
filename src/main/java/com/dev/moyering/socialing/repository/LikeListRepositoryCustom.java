package com.dev.moyering.socialing.repository;

import java.util.List;

public interface LikeListRepositoryCustom {
    List<Integer> findFeedIdsByUserId(Integer userId);
}
