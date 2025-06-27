package com.dev.moyering.gathering.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.PageRequest;

import org.springframework.data.domain.PageRequest;

import com.dev.moyering.gathering.dto.GatheringApplyDto;
import com.dev.moyering.gathering.dto.GatheringDto;
import com.dev.moyering.gathering.entity.Gathering;
import com.dev.moyering.user.entity.User;

public interface GatheringRepositoryCustom {

	void updateGathering(GatheringDto gatheringDto) throws Exception;

	// 메인 페이지에 추천 게더링 4개
	List<Gathering> findRecommendGatherRingForUser(User user) throws Exception;

	void updateGatheringStatus(Integer gatheringId, Boolean canceled) throws Exception;

	Long selectMyGatheringListCount(PageRequest pageRequest, Integer loginId, String word, String status);

	List<GatheringDto> selectMyGatheringList(PageRequest pageRequest, Integer loginId, String word, String status);

}
