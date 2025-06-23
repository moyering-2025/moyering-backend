package com.dev.moyering.gathering.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.moyering.gathering.entity.Gathering;
import com.dev.moyering.gathering.entity.GatheringLikes;
import com.dev.moyering.gathering.entity.QGathering;
import com.dev.moyering.gathering.repository.GatheringLikesRepository;
import com.dev.moyering.user.entity.User;
import com.querydsl.core.Tuple;
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

}
