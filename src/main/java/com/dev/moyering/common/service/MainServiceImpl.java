package com.dev.moyering.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.moyering.common.dto.MainSearchRequestDto;
import com.dev.moyering.gathering.dto.GatheringDto;
import com.dev.moyering.gathering.repository.GatheringRepository;
import com.dev.moyering.host.dto.HostClassDto;
import com.dev.moyering.host.repository.HostClassRepository;
import com.dev.moyering.socialing.dto.FeedDto;
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
	public Map<String,Object> searchAllBySearchQuery(MainSearchRequestDto dto) throws Exception {
		List<GatheringDto> gatheringList = gatheringRespoitory.findSearchGathering(dto).stream()
				.map(gathering -> gathering.toDto()).collect(Collectors.toList());
		List<HostClassDto> hostClassList = hostClassRepository.findSearchClass(dto).stream()
				.map(hostClass -> hostClass.toDto()).collect(Collectors.toList());
		List<FeedDto> feedList = feedRepository.findSearchFeed(dto).stream()
				.map(feed -> feed.toDto()).collect(Collectors.toList());
		
		Map<String,Object> map = new HashMap<>();
		map.put("feedList", feedList);
		map.put("gatheringList", gatheringList);
		map.put("hostClassList", hostClassList);
		
		return map;
	}

}
