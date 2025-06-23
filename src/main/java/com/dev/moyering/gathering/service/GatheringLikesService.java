package com.dev.moyering.gathering.service;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder.In;

import com.dev.moyering.gathering.dto.GatheringApplyDto;
import com.dev.moyering.user.dto.UserDto;
import com.querydsl.core.Tuple;

public interface GatheringLikesService {
	Integer getTotalLikesOfGatheringByGatheringId(Integer gatheringId) throws Exception;
	Boolean getGatheringLike(Integer gatheringId, Integer userId) throws Exception;
	Boolean toggleGatheringLike(Integer userId, Integer gatheringId) throws Exception;
	
}
