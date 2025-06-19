package com.dev.moyering.gathering.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.moyering.gathering.dto.GatheringApplyDto;
import com.dev.moyering.gathering.repository.GatheringApplyRepository;
import com.querydsl.core.Tuple;
@Service
public class GatheringApplyServiceImpl implements GatheringApplyService {

	@Autowired
	public GatheringApplyRepository gatheringApplyRepository;

	@Override
	public List<GatheringApplyDto> findApplyUserListByGatheringId(Integer gatheringId) throws Exception {
		return gatheringApplyRepository.findApplyUserListByGatheringId(gatheringId);
	}
}
