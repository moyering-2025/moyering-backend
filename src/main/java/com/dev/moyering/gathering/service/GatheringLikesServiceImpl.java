package com.dev.moyering.gathering.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.moyering.gathering.dto.GatheringLikesDto;
import com.dev.moyering.gathering.entity.Gathering;
import com.dev.moyering.gathering.entity.GatheringLikes;
import com.dev.moyering.gathering.repository.GatheringLikesRepository;
import com.dev.moyering.user.entity.User;
@Service
public class GatheringLikesServiceImpl implements GatheringLikesService {

	@Autowired
	public GatheringLikesRepository gatheringLikesRepository;

	@Override
	public Integer getTotalLikesOfGatheringByGatheringId(Integer gatheringId) throws Exception {

		Integer totalLikes = gatheringLikesRepository.countByGatheringId(gatheringId);
		System.out.println("totalLikes : "+totalLikes);
		return totalLikes;
	}

	@Override
	public Boolean getGatheringLike(Integer userId, Integer gatheringId) throws Exception {
		//좋아요 여부 조회
		return gatheringLikesRepository.selectGatheringLikes(userId, gatheringId)!=null;
	
	}

	@Override
	public Boolean toggleGatheringLike(Integer userId, Integer gatheringId) throws Exception {
		// 좋아요 상태 변경
		
		Integer gatheringLikeNum = gatheringLikesRepository.selectGatheringLikes(userId, gatheringId); 
		System.out.println("gatheringLikeNum : "+gatheringLikeNum);
		if(gatheringLikeNum==null) {
			gatheringLikesRepository.save(
					GatheringLikes.builder()
					.user(User.builder().userId(userId).build())
					.gathering(Gathering.builder().gatheringId(gatheringId).build())
					.build()
				);
			return true;
		} else {
			gatheringLikesRepository.deleteGatheringLikes(gatheringLikeNum);
			return false;
		}
	}

	@Override
	public List<GatheringLikesDto> getGatherlikeListByUserId(Integer userId) throws Exception {
		return gatheringLikesRepository.findAllByUser_userId(userId)
				.stream().map(gl -> gl.toDto()).collect(Collectors.toList());
	}

}
