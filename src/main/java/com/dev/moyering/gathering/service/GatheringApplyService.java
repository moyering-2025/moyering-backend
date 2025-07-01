package com.dev.moyering.gathering.service;

import java.util.List;

import com.dev.moyering.gathering.dto.GatheringApplyDto;
import com.dev.moyering.gathering.dto.GatheringDto;
import com.dev.moyering.user.dto.UserDto;
import com.dev.moyering.util.PageInfo;
import com.querydsl.core.Tuple;

public interface GatheringApplyService {

	List<GatheringApplyDto> findApplyUserListByGatheringId(Integer gatheringId)throws Exception;
	Integer findByGatheringIdAndUserId(Integer gatheringId, Integer userId)throws Exception;
	Integer applyToGathering(GatheringApplyDto gatheringApplyDto) throws Exception;
	List<GatheringApplyDto> findApplyUserListByGatheringIdForOrganizer(Integer gatheringId) throws Exception;
	void updateGatheringApplyApproval(Integer gatheringApplyId, boolean isApproved)throws Exception;
	List<GatheringApplyDto> findApplyListByApplyUserId(Integer userId)throws Exception;
}
