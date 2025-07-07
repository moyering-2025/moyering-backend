package com.dev.moyering.socialing.repository;

import com.dev.moyering.socialing.dto.ScrapListDto;
import com.dev.moyering.socialing.entity.Scrap;

import java.util.List;
import java.util.Optional;

public interface ScrapRepositoryCustom {
    Optional<Scrap> findScrapByUserIdAndFeedId(Integer userId, Integer feedId);

    List<Integer> findFeedIdsByUserId(Integer userId);

    void deleteByUserIdAndFeedId(Integer userId, Integer feedId);

    List<ScrapListDto> findMyScrapsCursor(Integer userId, Integer lastScrapId, int size);
}
