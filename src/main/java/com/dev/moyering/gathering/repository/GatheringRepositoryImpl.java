package com.dev.moyering.gathering.repository;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Objects;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import com.dev.moyering.gathering.dto.GatheringApplyDto;
import com.dev.moyering.gathering.dto.GatheringDto;
import com.dev.moyering.gathering.entity.Gathering;
import com.dev.moyering.gathering.entity.QGathering;
import com.dev.moyering.user.entity.User;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GatheringRepositoryImpl implements GatheringRepositoryCustom {
	@Autowired
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Long selectMyGatheringListCount(PageRequest pageRequest, Integer loginId, String word, String status) {
	    QGathering gathering = QGathering.gathering;
	    
	    BooleanExpression condition = gathering.user.userId.eq(loginId);
	    if (word != null && word.trim().length() > 0) {
	        condition = condition.and(gathering.title.contains(word));
	    }
	    BooleanExpression statusCondition = getStatusCondition(gathering, status);
	    if (statusCondition != null) {
	        condition = condition.and(statusCondition);
	    }
	    Long count = jpaQueryFactory.select(gathering.count())
	            .from(gathering)
	            .where(condition)
	            .fetchOne();
	    return count != null ? count : 0L;
	}
	@Override
//	public List<GatheringDto> selectMyGatheringList(PageRequest pageRequest, Integer loginId, String word, String status){	
//	    QGathering gathering = QGathering.gathering;
//	    List<Gathering> gatheringList = null;
//	    BooleanExpression condition = gathering.user.userId.eq(loginId);
//	    if (word != null && word.trim().length() > 0) {
//	        condition = condition.and(gathering.title.contains(word));
//	    }
//	    BooleanExpression statusCondition = getStatusCondition(gathering, status);
//	    if (statusCondition != null) {
//	        condition = condition.and(statusCondition); 
//	    }
//	    gatheringList = jpaQueryFactory.selectFrom(gathering)
//	            .where(condition)  
//	            .orderBy(gathering.gatheringId.desc())
//	            .offset(pageRequest.getOffset())
//	            .limit(pageRequest.getPageSize())
//	            .fetch();
//	    
//	    return gatheringList.stream()
//	            .map(Gathering::toDto)
//	            .collect(Collectors.toList());
//	}
	public List<GatheringDto> selectMyGatheringList(PageRequest pageRequest, Integer loginId, String word, String status){	
	    QGathering gathering = QGathering.gathering;
	    List<Gathering> gatheringList = null;
	    BooleanExpression condition = gathering.user.userId.eq(loginId);
	    if (word != null && word.trim().length() > 0) {
	        condition = condition.and(gathering.title.contains(word));
	    }
	    BooleanExpression statusCondition = getStatusCondition(gathering, status);
	    if (statusCondition != null) {
	        condition = condition.and(statusCondition); 
	    }
	    
	    // 정렬 조건 설정
	    JPAQuery<Gathering> query = jpaQueryFactory.selectFrom(gathering)
	            .where(condition);
	    
	    // status가 "진행예정"인 경우 특별한 정렬 적용
	    if ("진행예정".equals(status)) {
	        query = query.orderBy(
	            gathering.meetingDate.asc(),      // 날짜 오름차순 (가까운 날짜부터)
	            gathering.startTime.asc(),        // 시간 오름차순 (이른 시간부터)
	            gathering.gatheringId.desc()      // ID 내림차순 (같은 시간이면 최신 등록순)
	        );
	    } else {
	        query = query.orderBy(gathering.gatheringId.desc());
	    }
	    
	    gatheringList = query
	            .offset(pageRequest.getOffset())
	            .limit(pageRequest.getPageSize())
	            .fetch();
	    
	    return gatheringList.stream()
	            .map(Gathering::toDto)
	            .collect(Collectors.toList());
	}

	private BooleanExpression getStatusCondition(QGathering gathering, String status) {
	    
	    if (status == null || "전체".equals(status)) {
	        return null;
	    }
	    LocalDate today = LocalDate.now();         
	    LocalTime currentTime = LocalTime.now();    
	    Date todaySqlDate = Date.valueOf(today);   
	    
	    switch (status) {
	        case "진행예정":
	            return gathering.meetingDate.gt(todaySqlDate)
	                    .or(gathering.meetingDate.eq(todaySqlDate)   
	                            .and(gathering.startTime.gt(currentTime)));
	        case "진행완료":
	            return (gathering.meetingDate.lt(todaySqlDate) 
	                    .or(gathering.meetingDate.eq(todaySqlDate)    
	                            .and(gathering.startTime.lt(currentTime))))  
	                    .and(gathering.canceled.isFalse()); 
	        case "취소된 모임":
	            return gathering.canceled.isTrue(); 
	        default:
	            return null;
	    }
	}
	
	@Transactional
	public void updateGathering(GatheringDto gatheringDto) throws Exception{
		QGathering gathering = QGathering.gathering;
		JPAUpdateClause clause = jpaQueryFactory.update(gathering)
				.set(gathering.title, gatheringDto.getTitle())
				.set(gathering.gatheringContent, gatheringDto.getGatheringContent())
				.set(gathering.tags, gatheringDto.getTags())
				.set(gathering.subCategory.subCategoryId, gatheringDto.getSubCategoryId())
				.set(gathering.meetingDate, gatheringDto.getMeetingDate())
				.set(gathering.address, gatheringDto.getAddress())
				.set(gathering.detailAddress, gatheringDto.getDetailAddress())
				.set(gathering.preparationItems, gatheringDto.getPreparationItems())
				.set(gathering.intrOnln, gatheringDto.getIntrOnln())//한줄 소개
				.set(gathering.minAttendees, gatheringDto.getMinAttendees())
				.set(gathering.maxAttendees, gatheringDto.getMaxAttendees())
				.set(gathering.startTime, LocalTime.parse(gatheringDto.getStartTime()))
				.set(gathering.endTime, LocalTime.parse(gatheringDto.getEndTime()))
				.set(gathering.applyDeadline, gatheringDto.getApplyDeadline())
				.set(gathering.latitude, gatheringDto.getLatitude())
				.set(gathering.locName, gatheringDto.getLocName())
				.set(gathering.longitude, gatheringDto.getLongitude())
				.where(gathering.gatheringId.eq(gatheringDto.getGatheringId()));
		if(gatheringDto.getThumbnailFileName()!=null && !gatheringDto.getThumbnailFileName().equals("")) {
			clause.set(gathering.thumbnail, gatheringDto.getThumbnailFileName());
		}
		clause.execute();
	}
	@Override

	public void updateGatheringStatus(Integer gatheringId, Boolean canceled) throws Exception{
		QGathering gathering = QGathering.gathering;
		JPAUpdateClause clause = jpaQueryFactory.update(gathering)
				.set(gathering.canceled, canceled)
				.where(gathering.gatheringId.eq(gatheringId));
		clause.execute();
	}
	@Override
	public List<Gathering> findRecommendGatherRingForUser(User user) throws Exception {
		QGathering gathering = QGathering.gathering;
		BooleanBuilder builder = new BooleanBuilder();
	    builder.and(gathering.canceled.isFalse());
		builder.and(gathering.meetingDate.goe(Date.valueOf(LocalDate.now())));
		
		if (user != null) {
	        List<String> preferences = Stream.of(
	            user.getCategory1(), user.getCategory2(),
	            user.getCategory3(), user.getCategory4(), user.getCategory5()
	        ).filter(Objects::nonNull).collect(Collectors.toList());
        	
	        if (!preferences.isEmpty()) {
	            builder.and(
            		gathering.subCategory.subCategoryName.in(preferences)
	            );
	        }
	    }
		List<Gathering> preferred = jpaQueryFactory
				.select(gathering)
				.from(gathering)
				.where(builder)
				.orderBy(gathering.meetingDate.asc())
				.limit(4)
				.fetch();
		
		List<Integer> preferredIds = preferred.stream()
			    .map(Gathering::getGatheringId)
			    .collect(Collectors.toList());

			// 2단계: 부족하면 나머지로 채우기
			if (preferred.size() < 4) {
			    List<Gathering> fallback = jpaQueryFactory
			        .selectFrom(gathering)
			        .where(
			            gathering.canceled.isFalse()
			                .and(gathering.meetingDate.goe(Date.valueOf(LocalDate.now())))
			                .and(preferredIds.isEmpty() ? null : gathering.gatheringId.notIn(preferredIds))
			        )
			        .orderBy(gathering.meetingDate.asc())
			        .limit(4 - preferred.size())
			        .fetch();

			    preferred.addAll(fallback);
			}

		return preferred;
	}
  
//  @Override
//	public List<GatheringDto> selectGatheringByUserIdAndPaging(PageRequest pageRequest, Integer loginId, String word){	
//		QGathering gathering = QGathering.gathering;
//		List<Tuple> gatheringList = null;
//		if(word==null || word.trim().length()==0) {//검색어 없는 경우
//			gatheringList = jpaQueryFactory.selectFrom(gathering)
//					.where(gathering.user.userId.eq(loginId))//등록한 사람이 로그인 아이디와 일치하는 경우에만.
//					.orderBy(gathering.gatheringId.desc())
//					.offset(pageRequest.getOffset())
//					.limit(pageRequest.getPageSize())
//					.fetch();
//		} else { // 검색어 있는 경우
//		    gatheringList = jpaQueryFactory.selectFrom(gathering)
//		            .where(gathering.user.userId.eq(loginId)
//		                .and(gathering.title.contains(word))) // 제목에서 검색
//		            .orderBy(gathering.gatheringId.desc())
//		            .offset(pageRequest.getOffset())
//		            .limit(pageRequest.getPageSize())
//		            .fetch();
//		}
//
//		return gatheringList.stream()
//				.map(t-> {
//					Gathering g = t.get(0,Gathering.class);
//					GatheringDto gatheringDto = modelMapper.map(g, GatheringDto.class);
//					return gatheringDto;
//				}).collect(Collectors.toList());
//	}
//  
  
  
  
}
