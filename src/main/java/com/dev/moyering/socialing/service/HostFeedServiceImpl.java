package com.dev.moyering.socialing.service;

import com.dev.moyering.socialing.dto.HostFeedDto;
import com.dev.moyering.socialing.repository.HostFeedRepository;
import com.dev.moyering.socialing.repository.HostFeedRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HostFeedServiceImpl implements HostFeedService {

    private final HostFeedRepository hostFeedRepository;

    @Override
    public List<HostFeedDto> getHostFeeds(Integer hostId, int offset, int size) throws Exception {
        return hostFeedRepository.findHostFeeds(hostId, offset, size);
    }

    @Override
    public List<HostFeedDto> getHostFeedsByCategory(Integer hostId, String category, int offset, int size) throws Exception {
        return hostFeedRepository.findHostFeedsByCategory(hostId, category, offset, size);
    }

    @Override
    public HostFeedDto getHostFeedDetail(Integer feedId) throws Exception {
        return hostFeedRepository.findById(feedId)
                .map(feed -> HostFeedDto.builder()
                        .feedId(feed.getFeedId())
                        .content(feed.getContent())
                        .img1(feed.getImg1())
                        .img2(feed.getImg2())
                        .img3(feed.getImg3())
                        .img4(feed.getImg4())
                        .img5(feed.getImg5())
                        .createDate(feed.getCreateDate())
                        .updateDate(feed.getUpdateDate())
                        .isDeleted(feed.getIsDeleted())
                        .tag1(feed.getTag1())
                        .tag2(feed.getTag2())
                        .tag3(feed.getTag3())
                        .tag4(feed.getTag4())
                        .tag5(feed.getTag5())
                        .category(feed.getCategory())
                        .hostId(feed.getHost().getHostId())
                        .hostName(feed.getHost().getName())
                        .hostProfile(feed.getHost().getProfile())
                        .build())
                .orElseThrow(() -> new Exception("해당 피드가 존재하지 않습니다."));
    }
}
