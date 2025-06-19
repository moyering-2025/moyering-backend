package com.dev.moyering.gathering.service;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dev.moyering.gathering.dto.GatheringDto;
import com.dev.moyering.gathering.entity.Gathering;
import com.dev.moyering.gathering.repository.GatheringRepository;
import com.dev.moyering.util.PageInfo;

@Service
public class GatheringServiceImpl implements GatheringService {
	@Value("${iupload.path}")
	private String iuploadPath;

	@Value("${dupload.path}")
	private String duploadPath;
	@Autowired
	public GatheringRepository gatheringRepository;
	@Override
	public Integer writeGathering(GatheringDto gatheringDto, MultipartFile thumbnail) throws Exception {
		// 게더링 등록
		if(thumbnail!=null && !thumbnail.isEmpty()) {
			File upFile = new File(iuploadPath, thumbnail.getOriginalFilename());
			thumbnail.transferTo(upFile);
			gatheringDto.setThumbnailFileName(thumbnail.getOriginalFilename());
		}
		Gathering gathering = gatheringDto.toEntity();
		gatheringRepository.save(gathering);
		return gathering.getGatheringId();
	}
	@Override
	public void modifyGathering(GatheringDto gatheringDto, MultipartFile thumbnail) throws Exception {
		// 게더링 수정
		if(thumbnail!=null && !thumbnail.isEmpty()) {
			File upFile = new File(iuploadPath, thumbnail.getOriginalFilename());
			thumbnail.transferTo(upFile);
			gatheringDto.setThumbnailFileName(thumbnail.getOriginalFilename());
		}
		gatheringRepository.updateGathering(gatheringDto);
	}
	
	@Override
	public List<GatheringDto> myGatheringList(Integer userId, PageInfo pageInfo, String word) throws Exception {
		// 내가 등록한 게더링 목록 + 페이지네이션, 제목으로 검색 
		PageRequest pageRequest = PageRequest.of(pageInfo.getCurPage()-1, 10);
		
		return null;
	}
	@Override
	public Boolean getGatheringLike(Integer userId, Integer gatheringId) throws Exception {
		//좋아요 여부 조회
//		return gatheringDslRepository.selectGatheringLike(userId, gatheringId)!=null;
		return null;
	}
	@Override
	public Boolean toggleGatheringLike(Integer userId, Integer gatheringId) throws Exception {
		// 좋아요 상태 변경
		return null;
//		Integer gatheringLikeNum = gatheringRepository.selectGatheringLike(userId, gatheringId); 
//		if(gatheringLikeNum==null) {
//			gatheringRepository.save(
//					GatheringLike.builder()
//					.User(User.builder().userId(userId).build())
//					.gathering(Gathering.builder().gatheringId(gatheringId).build()).build());
//			return true;
//		} else {
//			gatheringDslRepository.deleteGatheringLike(gatheringLikeNum);
//			return false;
//		}
	}
	@Override
	public List<GatheringDto> myGatheringApplyList(Integer userId, PageInfo pageInfo, String word) throws Exception {
		// 내가 지원한 게더링 목록 + 페이지네이션, 제목으로 검색 
		PageRequest pageRequest = PageRequest.of(pageInfo.getCurPage()-1, 10);
		return null;
	}
	@Override
	public GatheringDto detailGathering(Integer gatheringId) throws Exception {
		// 게더링 조회
		Gathering gathering = gatheringRepository.findById(gatheringId).orElseThrow(()->new Exception("조회 중 오류"));
		return gathering.toDto();
	}
	@Override
	public List<GatheringDto> getMainGathersForUser(Integer userId) throws Exception {
		//메인페이지에 취향에 맞는 게더링 4개 가져오기
		List<Gathering> gathers;
		if (userId == null) {
			gathers = gatheringRepository.findRecommendGatherRingForUser(null);
		}
		return null;
	}	
}
