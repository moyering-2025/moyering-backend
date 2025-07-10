package com.dev.moyering.gathering.service;

import java.io.File;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dev.moyering.common.dto.GatheringSearchRequestDto;
import com.dev.moyering.common.dto.PageResponseDto;
import com.dev.moyering.gathering.dto.GatheringDto;
import com.dev.moyering.gathering.entity.Gathering;
import com.dev.moyering.gathering.entity.QGathering;
import com.dev.moyering.common.repository.SubCategoryRepository;
import com.dev.moyering.gathering.dto.GatheringApplyDto;
import com.dev.moyering.gathering.dto.GatheringDto;
import com.dev.moyering.gathering.entity.Gathering;
import com.dev.moyering.gathering.repository.GatheringApplyRepository;
import com.dev.moyering.gathering.repository.GatheringRepository;
import com.dev.moyering.gathering.repository.MessageRepository;
import com.dev.moyering.user.entity.User;
import com.dev.moyering.user.repository.UserRepository;
import com.dev.moyering.util.PageInfo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GatheringServiceImpl implements GatheringService {
	@Value("${iupload.path}")
	private String iuploadPath;

	@Value("${dupload.path}")
	private String duploadPath;
	@Autowired
	public GatheringRepository gatheringRepository;
	@Autowired
	public GatheringApplyRepository gatheringApplyRepository;
	@Autowired
	private final UserRepository userRepository;
	@Autowired
	private MessageRepository messageRepository;
	private final JPAQueryFactory jpaQueryFactory;
	
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
	public List<GatheringDto> myGatheringList(Integer userId, PageInfo pageInfo, String word, String status) throws Exception {
	    // 내가 등록한 게더링 목록 + 페이지네이션, 제목으로 검색 
	    PageRequest pageRequest = PageRequest.of(pageInfo.getCurPage()-1, 10);
	    Long cnt = gatheringRepository.selectMyGatheringListCount(userId, word, status);
	    
	    Integer allPage = (int)(Math.ceil(cnt.doubleValue()/pageRequest.getPageSize()));
	    Integer startPage = (pageInfo.getCurPage()-1)/10*10+1;
	    Integer endPage = Math.min(startPage+10-1, allPage);
	    
	    pageInfo.setAllPage(allPage);
	    pageInfo.setStartPage(startPage);
	    pageInfo.setEndPage(endPage);
	    
	    // 1. 기본 게더링 목록 조회
	    List<GatheringDto> gatheringList = gatheringRepository.selectMyGatheringList(pageRequest, userId, word, status);
	    
	    // 2. 각 게더링별 참여 인원수 정보 추가
	    for (GatheringDto gathering : gatheringList) {
	        Integer gatheringId = gathering.getGatheringId();
	        
	        // 참여 수락된 인원수 조회
	        Integer acceptedCount = gatheringApplyRepository.countByGatheringGatheringIdAndIsApprovedTrue(gatheringId).intValue();
	        // 전체 참여신청 인원수 조회  
	        Integer appliedCount = gatheringApplyRepository.countByGatheringGatheringId(gatheringId).intValue();
	        
	        // DTO에 값 설정
	        gathering.setAcceptedCount(acceptedCount != null ? acceptedCount : 0);
	        gathering.setAppliedCount(appliedCount != null ? appliedCount : 0);
	    }
	    
	    return gatheringList;
	}
	@Override
	public Integer selectMyGatheringListCount(Integer userId, String word, String status) throws Exception {
		return gatheringRepository.selectMyGatheringListCount(userId, word, status).intValue();
	}
	@Override
	public GatheringDto detailGathering(Integer gatheringId) throws Exception {
		// 게더링 조회
		Gathering gathering = gatheringRepository.findById(gatheringId).orElseThrow(()->new Exception("조회 중 오류"));
		return gathering.toDto();
	}
	
	@Override
	public void updateGatheringStatus(Integer gatheringId, Boolean canceled) throws Exception {
		//주최자 시점 상태 변경 모임 취소 등.. 
		gatheringRepository.updateGatheringStatus(gatheringId, canceled);
	}
	@Override
	public List<GatheringDto> getMainGathersForUser(Integer userId) throws Exception {
		//메인페이지에 취향에 맞는 게더링 4개 가져오기
		List<Gathering> gathers;
		if (userId == null) {
			gathers = gatheringRepository.findRecommendGatherRingForUser(null);
		}else {
	        User user = userRepository.findById(userId)
	                .orElseThrow(() -> new Exception("해당 사용자를 찾을 수 없습니다: id=" + userId));
	        gathers = gatheringRepository.findRecommendGatherRingForUser(user);
	    }		

        return gathers.stream()
        		.map(g -> {
                    GatheringDto dto = g.toDto();
                    return dto;
                })
                .collect(Collectors.toList());
	}
	@Override
	public PageResponseDto<GatheringDto> searchGathers(GatheringSearchRequestDto dto) throws Exception {
		Pageable pageable = PageRequest.of(dto.getPage(), dto.getSize());

		QGathering gathering = QGathering.gathering;
		
		BooleanBuilder builder = new BooleanBuilder();
		
		//모집 중이면서~~ 오늘 이후의 일정
		builder.and(gathering.canceled.isFalse());
		builder.and(gathering.meetingDate.goe(Date.valueOf(LocalDate.now())));
		
		//검색 조건 필터 추가
		if (dto.getSido() !=null && !dto.getSido().isBlank()) {
			builder.and(gathering.locName.contains(dto.getSido()));
		}
		if (dto.getCategory1() != null) {
		 builder.and(gathering.subCategory.firstCategory.categoryId.eq(dto.getCategory1()));
		}
	    if (dto.getCategory2() != null) {
	        builder.and(gathering.subCategory.subCategoryId.eq(dto.getCategory2()));
	    }
	    if (dto.getStartDate() != null) {
	        builder.and(gathering.meetingDate.goe(Date.valueOf(dto.getStartDate())));
	    }
	    if (dto.getEndDate() != null) {
	        builder.and(gathering.meetingDate.loe(Date.valueOf(dto.getEndDate())));
	    }
	    if (dto.getMinAttendees() != null) {
	        builder.and(gathering.minAttendees.goe(dto.getMinAttendees()));
	    }
	    if (dto.getMaxAttendees() != null) {
	        builder.and(gathering.maxAttendees.loe(dto.getMaxAttendees()));
	    }
		if (dto.getTitle() != null) {
			 builder.and(gathering.title.contains(dto.getTitle()));
		}
		List<Gathering> result = jpaQueryFactory
				.select(gathering)
				.from(gathering)
				.where(builder)
	            .offset(pageable.getOffset())
	            .limit(pageable.getPageSize())
	            .fetch();
	    //총 개수 (distinct count!)
	    Long total = jpaQueryFactory
	            .select(gathering.countDistinct())
	            .from(gathering)
	            .where(builder)
	            .fetchOne();
		List<GatheringDto> dtoList = result.stream()
				.map(g -> {
					GatheringDto dto2 = g.toDto();
					return dto2;
				}).collect(Collectors.toList());
		
		return PageResponseDto.<GatheringDto>builder()
				.content(dtoList)
	            .currentPage(dto.getPage() + 1)
	            .totalPages((int) Math.ceil((double) total / dto.getSize()))
	            .totalElements(total)
	            .build();
	}
	@Override
	public List<GatheringDto> findGatheringWithCategory(Integer originalGatheringId, Integer subCategoryId, Integer categoryId) throws Exception {
		return gatheringRepository.findRecommendGatheringForUser(originalGatheringId, subCategoryId, categoryId);
	}
	@Override
	public List<GatheringDto> getMyApplyGatheringSchedule(Integer userId) throws Exception {
		return gatheringRepository.findMyApplyGatheringSchedule(userId);
	}
	@Override
	public List<GatheringDto> getMyGatheringSchedule(Integer userId) throws Exception {
		return gatheringRepository.findMyGatheringSchedule(userId);
	}

}
