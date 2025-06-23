package com.dev.moyering.gathering.repository;

import java.util.List;

import com.dev.moyering.gathering.dto.GatheringApplyDto;
import com.dev.moyering.gathering.entity.Gathering;
import com.dev.moyering.gathering.entity.GatheringApply;
import com.querydsl.core.Tuple;

public interface GatheringLikesRepositoryCustom {
	Integer countByGatheringId(Integer gatheringId) throws Exception; 
	Integer selectGatheringLikes(Integer userId, Integer gatheringId) throws Exception;
	void deleteGatheringLikes(Integer gatheringLikeId)throws Exception;
}
