package com.dev.moyering.gathering.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.moyering.gathering.entity.GatheringLikes;

public interface GatheringLikesRepository extends JpaRepository<GatheringLikes, Integer>, GatheringLikesRepositoryCustom {
	//메인페이지 사용자의 게더링 좋아요 리스트
	List<GatheringLikes> findAllByUser_userId(Integer userId) throws Exception;
	Long countByGatheringGatheringId(Integer gatheringId) throws Exception;
//	Optional<Integer> findGatheringLikeIdByGatheringGatheringIdAndUserUserId(Integer gatheringId, Integer userId);
}
