package com.dev.moyering.socialing.service;

import com.dev.moyering.socialing.dto.HostFeedDto;

import java.util.List;

public interface HostFeedService {
    List<HostFeedDto> getHostFeeds(Integer hostId, int offset, int size) throws Exception;
    List<HostFeedDto> getHostFeedsByCategory(Integer hostId, String category, int offset, int size) throws Exception;
    HostFeedDto getHostFeedDetail(Integer feedId) throws Exception;
}
