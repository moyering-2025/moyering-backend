package com.dev.moyering.socialing.repository;

import com.dev.moyering.socialing.dto.HostFeedDto;

import java.util.List;

public interface HostFeedRepositoryCustom {
    List<HostFeedDto> findHostFeeds(int offset, int size);

    List<HostFeedDto> findHostFeedsByCategory(Integer hostId, String category, int offset, int size);
}
