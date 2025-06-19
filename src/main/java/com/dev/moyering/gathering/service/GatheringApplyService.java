package com.dev.moyering.gathering.service;

import java.util.List;

import com.dev.moyering.gathering.dto.GatheringApplyDto;
import com.querydsl.core.Tuple;

public interface GatheringApplyService {

	public List<GatheringApplyDto> findApplyUserListByGatheringId(Integer gatheringId)throws Exception;

}
