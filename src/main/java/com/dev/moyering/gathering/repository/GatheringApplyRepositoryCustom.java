package com.dev.moyering.gathering.repository;

import java.util.List;

import com.dev.moyering.gathering.dto.GatheringApplyDto;
import com.dev.moyering.gathering.entity.Gathering;
import com.dev.moyering.gathering.entity.GatheringApply;
import com.querydsl.core.Tuple;

public interface GatheringApplyRepositoryCustom {
	
	List<GatheringApplyDto> findApplyUserListByGatheringId(Integer gatheringId) throws Exception;
}
