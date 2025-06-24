package com.dev.moyering.gathering.service;

import java.util.List;

import com.dev.moyering.gathering.dto.GatheringApplyDto;
import com.dev.moyering.gathering.dto.GatheringDto;
import com.dev.moyering.user.dto.UserDto;
import com.dev.moyering.util.PageInfo;
import com.querydsl.core.Tuple;

public interface GatheringApplyService {

	public List<GatheringApplyDto> findApplyUserListByGatheringId(Integer gatheringId)throws Exception;

	public Integer findByGatheringIdAndUserId(Integer gatheringId, Integer userId)throws Exception;
	public Integer applyToGathering(GatheringApplyDto gatheringApplyDto) throws Exception;
}
