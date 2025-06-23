package com.dev.moyering.gathering.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.dev.moyering.gathering.dto.GatheringApplyDto;
import com.dev.moyering.gathering.dto.GatheringDto;
import com.dev.moyering.util.PageInfo;

public interface GatheringService {

	Integer writeGathering(GatheringDto gatheringDto, MultipartFile thumbnail) throws Exception;
	void modifyGathering(GatheringDto gatheringDto, MultipartFile thumbnail) throws Exception;
	GatheringDto detailGathering (Integer gatheringId) throws Exception;
	List<GatheringDto> myGatheringList(Integer userId, PageInfo pageInfo, String word) throws Exception;

	List<GatheringDto> myGatheringApplyList(Integer userId, PageInfo pageInfo, String word) throws Exception;
	
	//메인페이지 게더링
	List<GatheringDto> getMainGathersForUser(Integer userId) throws Exception;
}
