package com.dev.moyering.gathering.repository;

import java.util.List;

import org.springframework.data.domain.PageRequest;

import com.dev.moyering.gathering.dto.GatheringApplyDto;
import com.dev.moyering.gathering.entity.Gathering;
import com.dev.moyering.gathering.entity.GatheringApply;
import com.querydsl.core.Tuple;

public interface GatheringApplyRepositoryCustom {
	
	List<GatheringApplyDto> findApprovedUserListByGatheringId(Integer gatheringId) throws Exception;
	void updateGatheringApplyApproval (Integer gatheringApplyId, boolean isApproved) throws Exception;
	List<GatheringApplyDto> findApplyUserListByGatheringIdForOrganizer(Integer gatheringId) throws Exception;
	Long findMyApplyListCount(Integer userId, String word, String status) throws Exception;
	List<GatheringApplyDto> getAppliedGatheringList(Integer loginId, String word, String status,
			PageRequest pageRequest);
}
