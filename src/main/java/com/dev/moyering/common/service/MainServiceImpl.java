package com.dev.moyering.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dev.moyering.common.dto.MainSearchRequestDto;
import com.dev.moyering.gathering.dto.GatheringDto;
import com.dev.moyering.gathering.entity.Gathering;
import com.dev.moyering.gathering.repository.GatheringRepository;
import com.dev.moyering.host.dto.HostClassDto;
import com.dev.moyering.host.entity.HostClass;
import com.dev.moyering.host.repository.HostClassRepository;
import com.dev.moyering.socialing.dto.FeedDto;
import com.dev.moyering.socialing.entity.Feed;
import com.dev.moyering.socialing.repository.FeedRepository;

@Service
public class MainServiceImpl implements MainService {

	@Autowired
	private GatheringRepository gatheringRespoitory;
	@Autowired
	private HostClassRepository hostClassRepository;
	@Autowired
	private FeedRepository feedRepository;

	@Override
	public Map<String, Object> searchAllBySearchQuery(MainSearchRequestDto dto) throws Exception {
	    Pageable pageable = PageRequest.of(dto.getPage(), dto.getSize());

	    // Gathering 데이터 페이지네이션
	    Page<Gathering> gatheringPage = gatheringRespoitory.findSearchGathering(dto, pageable);
	    Page<HostClass> hostClassPage = hostClassRepository.findSearchClass(dto, pageable);
	    Page<Feed> feedPage = feedRepository.findSearchFeed(dto, pageable);

	    List<GatheringDto> gatheringList = gatheringPage.stream().map(Gathering::toDto).collect(Collectors.toList());
	    List<HostClassDto> hostClassList = hostClassPage.stream().map(HostClass::toDto).collect(Collectors.toList());
	    List<FeedDto> feedList = feedPage.stream().map(Feed::toDto).collect(Collectors.toList());

	    Map<String, Object> map = new HashMap<>();
	    map.put("feedList", feedList);
	    map.put("gatheringList", gatheringList);
	    map.put("hostClassList", hostClassList);

	    // 페이지 정보 추가
	    map.put("totalPages", Math.max(
	        Math.max(gatheringPage.getTotalPages(), hostClassPage.getTotalPages()),
	        feedPage.getTotalPages()
	    ));
	    map.put("currentPage", dto.getPage() + 1); // 1부터 시작하는 페이지 번호로 변경

	    return map;
	}

}
