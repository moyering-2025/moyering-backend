package com.dev.moyering.gathering.service;

import java.util.List;

import com.dev.moyering.gathering.dto.GatheringApplyDto;
import com.dev.moyering.gathering.dto.GatheringDto;
import com.dev.moyering.user.dto.UserDto;
import com.dev.moyering.util.PageInfo;
import com.querydsl.core.Tuple;

public interface GatheringApplyService {

	List<GatheringApplyDto> findApprovedUserListByGatheringId(Integer gatheringId)throws Exception;
	Integer findByGatheringIdAndUserId(Integer gatheringId, Integer userId)throws Exception;
	Integer applyToGathering(GatheringApplyDto gatheringApplyDto) throws Exception;
	Integer findApprovedUserCountByGatheringId(Integer gatheringId)throws Exception;
	List<GatheringApplyDto> findApplyUserListByGatheringIdForOrganizer(Integer gatheringId) throws Exception;
	void updateGatheringApplyApproval(Integer gatheringApplyId, boolean isApproved)throws Exception;
	List<GatheringApplyDto> findApplyListByApplyUserId(Integer userId, PageInfo pageInfo, String word, String status)
			throws Exception;
	Integer selectMyApplyListCount(Integer userId, String word, String status) throws Exception;
	void cancelGatheringApply(Integer gatheringApplyId) throws Exception;
}
