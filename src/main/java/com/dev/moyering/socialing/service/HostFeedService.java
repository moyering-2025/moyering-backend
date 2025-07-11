package com.dev.moyering.socialing.service;

import com.dev.moyering.socialing.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface HostFeedService {
    List<HostFeedDto> getHostFeeds(int offset, int size, String category) throws Exception;
//    HostFeedDto getHostFeedDetail(Integer feedId) throws Exception;
    HostFeedResponseDto createHostFeed(Integer userId, HostFeedCreateDto dto, List<MultipartFile> images) throws Exception;

    HostFeedDetailDto getHostFeedDetail(Integer feedId) throws Exception;

    void updateHostFeed(Integer feedId, Integer userId, HostFeedUpdateDto dto, List<MultipartFile> images,List<String> removeUrls) throws Exception;

    void deleteHostFeed(Integer feedId, Integer userId) throws Exception;
}
