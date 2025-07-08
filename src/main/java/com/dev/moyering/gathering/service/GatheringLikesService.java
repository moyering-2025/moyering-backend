package com.dev.moyering.gathering.service;

import java.util.List;

import com.dev.moyering.gathering.dto.GatheringLikesDto;

public interface GatheringLikesService {
	Integer getTotalLikesOfGatheringByGatheringId(Integer gatheringId) throws Exception;
	Boolean getGatheringLike(Integer gatheringId, Integer userId) throws Exception;
	Boolean toggleGatheringLike(Integer userId, Integer gatheringId) throws Exception;
	//메인페이지 게더링 좋아요리스트
	List<GatheringLikesDto> getGatherlikeListByUserId(Integer userId) throws Exception;
	
}
