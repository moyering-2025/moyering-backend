package com.dev.moyering.gathering.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dev.moyering.classring.dto.UtilSearchDto;
import com.dev.moyering.classring.dto.WishlistItemDto;
import com.dev.moyering.gathering.dto.GatheringApplyDto;
import com.dev.moyering.gathering.entity.Gathering;
import com.dev.moyering.gathering.entity.GatheringApply;
import com.querydsl.core.Tuple;

public interface GatheringLikesRepositoryCustom {
	Integer countByGatheringId(Integer gatheringId) throws Exception; 
	Integer selectGatheringLikes(Integer userId, Integer gatheringId) throws Exception;
	void deleteGatheringLikes(Integer gatheringLikeId)throws Exception;
	Page<WishlistItemDto> searchByUser(UtilSearchDto dto, Pageable pageable) throws Exception;
}
