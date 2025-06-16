package com.dev.moyering.gathering.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.dev.moyering.gathering.dto.GatheringDto;
import com.dev.moyering.util.PageInfo;

public interface GatheringService {

	Integer writeGathering(GatheringDto gatheringDto, MultipartFile ifile) throws Exception;

	void modifyGathering(GatheringDto gatheringDto, MultipartFile ifile) throws Exception;
	GatheringDto detailGathering (Integer gatheringId) throws Exception;
	List<GatheringDto> myGatheringList(Integer userId, PageInfo pageInfo, String word) throws Exception;

	Boolean getGatheringLike(Integer userId, Integer gatheringId) throws Exception;

	Boolean toggleGatheringLike(Integer userId, Integer gatheringId) throws Exception;

	List<GatheringDto> myGatheringApplyList(Integer userId, PageInfo pageInfo, String word) throws Exception;
	
}
