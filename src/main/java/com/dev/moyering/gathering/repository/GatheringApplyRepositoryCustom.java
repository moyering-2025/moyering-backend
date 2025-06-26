package com.dev.moyering.gathering.repository;

import java.util.List;

import com.dev.moyering.gathering.dto.GatheringApplyDto;
import com.dev.moyering.gathering.entity.Gathering;
import com.dev.moyering.gathering.entity.GatheringApply;
import com.querydsl.core.Tuple;

public interface GatheringApplyRepositoryCustom {
	
	List<GatheringApplyDto> findApplyUserListByGatheringId(Integer gatheringId) throws Exception;
	void updateGatheringApplyApproval (Integer gatheringApplyId, boolean isApproved) throws Exception;
	Integer findByGatheringIdAndUserId(Integer gatheringId, Integer userId) throws Exception;
	List<GatheringApplyDto> findApplyUserListByGatheringIdForOrganizer(Integer gatheringId) throws Exception;
}
