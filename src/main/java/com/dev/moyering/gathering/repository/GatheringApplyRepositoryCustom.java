package com.dev.moyering.gathering.repository;

import java.util.List;

import com.dev.moyering.gathering.dto.GatheringApplyDto;
import com.dev.moyering.gathering.entity.Gathering;
import com.dev.moyering.gathering.entity.GatheringApply;
import com.querydsl.core.Tuple;

public interface GatheringApplyRepositoryCustom {
	
	List<GatheringApplyDto> findApplyUserListByGatheringId(Integer gatheringId) throws Exception;
	void updateMemberApproval (Integer gatheringId, Integer userId, boolean isApproved) throws Exception;
	void applyToGathering(GatheringApplyDto gatheringApplyDto) throws Exception;
	
}
