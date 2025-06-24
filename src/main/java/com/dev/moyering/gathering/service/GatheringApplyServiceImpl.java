package com.dev.moyering.gathering.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.dev.moyering.gathering.dto.GatheringApplyDto;
import com.dev.moyering.gathering.dto.GatheringDto;
import com.dev.moyering.gathering.entity.Gathering;
import com.dev.moyering.gathering.entity.GatheringApply;
import com.dev.moyering.gathering.repository.GatheringApplyRepository;
import com.dev.moyering.util.PageInfo;
import com.querydsl.core.Tuple;
@Service
public class GatheringApplyServiceImpl implements GatheringApplyService {

	@Autowired
	public GatheringApplyRepository gatheringApplyRepository;

	@Override
	public List<GatheringApplyDto> findApplyUserListByGatheringId(Integer gatheringId) throws Exception {
		return gatheringApplyRepository.findApplyUserListByGatheringId(gatheringId);
	}

	@Override
	public Integer findByGatheringIdAndUserId(Integer gatheringId, Integer userId) throws Exception {
		return gatheringApplyRepository.findBygatheringIdAnduserId(gatheringId, userId);
	} 
	@Override
	public Integer applyToGathering(GatheringApplyDto gatheringApplyDto) throws Exception {
		
		GatheringApply gatheringApply = gatheringApplyDto.toEntity();
		gatheringApplyRepository.save(gatheringApply);
		return gatheringApply.getGatheringApplyId();
	}
}
