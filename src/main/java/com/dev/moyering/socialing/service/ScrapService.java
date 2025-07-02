package com.dev.moyering.socialing.service;

import com.dev.moyering.socialing.dto.ScrapDto;

import java.util.List;

public interface ScrapService {
    ScrapDto createScrap(Integer userId, Integer feedId)throws Exception;

    void deleteScrap(Integer userId, Integer feedId) throws Exception;

    List<Integer> getScrapFeedIds(Integer userId);

    boolean isScrapped(Integer userId, Integer feedId);
}
