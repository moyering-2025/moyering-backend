package com.dev.moyering.socialing.service;

import com.dev.moyering.socialing.dto.ScrapDto;
import com.dev.moyering.socialing.dto.ScrapListDto;

import java.util.List;

public interface ScrapService {
    ScrapDto createScrap(Integer userId, Integer feedId)throws Exception;

    void deleteScrap(Integer userId, Integer feedId) throws Exception;

    List<Integer> getScrapFeedIds(Integer userId);

    boolean isScrapped(Integer userId, Integer feedId);

    List<ScrapListDto> getMyScrapsCursor(Integer userId, Integer lastScrapId, int size);
}
