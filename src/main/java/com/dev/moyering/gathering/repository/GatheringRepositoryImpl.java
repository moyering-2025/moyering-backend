package com.dev.moyering.gathering.repository;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import com.dev.moyering.gathering.dto.GatheringDto;
import com.dev.moyering.gathering.entity.Gathering;
import com.dev.moyering.gathering.entity.QGathering;
import com.dev.moyering.user.entity.User;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GatheringRepositoryImpl implements GatheringRepositoryCustom {
	@Autowired
	private final JPAQueryFactory jpaQueryFactory;
	
	public Long selectMyGatheringListCount(PageRequest pageRequest, Integer loginId, String word){	
		QGathering gathering = QGathering.gathering;
		if(word==null || word.trim().length()==0) {//검색어 없는 경우
			return jpaQueryFactory.select(gathering.count())
					.where(gathering.user.userId.eq(loginId)
							.and(gathering.title.contains(word))) // 제목에서 검색
					.from(gathering)
					.fetchOne();
		}
		Long cnt = 0L;
		cnt = jpaQueryFactory.select(gathering.count())
				.where(gathering.user.userId.eq(loginId)
						.and(gathering.title.contains(word))) // 제목에서 검색
				.from(gathering)
				.fetchOne();
		return cnt;
	}
	
	@Transactional
	public void updateGathering(GatheringDto gatheringDto) throws Exception{
		QGathering gathering = QGathering.gathering;
		LocalTime startTime = LocalTime.parse(gatheringDto.getStartTime());
		LocalTime endTime = LocalTime.parse(gatheringDto.getEndTime());
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
				.set(gathering.startTime, startTime)
				.set(gathering.endTime, endTime)
				.set(gathering.applyDeadline, gatheringDto.getApplyDeadline())
				.where(gathering.gatheringId.eq(gatheringDto.getGatheringId()));
		if(gatheringDto.getThumbnailFileName()!=null && !gatheringDto.getThumbnailFileName().equals("")) {
			clause.set(gathering.thumbnail, gatheringDto.getThumbnailFileName());
		}
		clause.execute();
	}
	public void updateGatheringStatus(Integer gatheringId, String status) throws Exception{
		QGathering gathering = QGathering.gathering;
		JPAUpdateClause clause = jpaQueryFactory.update(gathering)
				.set(gathering.status, status)
				.where(gathering.gatheringId.eq(gatheringId));
		clause.execute();
	}
	@Override
	public List<Gathering> findRecommendGatherRingForUser(User user) throws Exception {
		QGathering gathering = QGathering.gathering;
		BooleanBuilder builder = new BooleanBuilder();
	    builder.and(gathering.status.eq("모집중"));
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
			            gathering.status.eq("모집중")
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
  
  @Override
	public List<Gathering> selectMyGatheringList(PageRequest pageRequest, Integer loginId, String word){	
		QGathering gathering = QGathering.gathering;
		List<Gathering> gatheringList = null;
		if(word==null || word.trim().length()==0) {//검색어 없는 경우
			gatheringList = jpaQueryFactory.selectFrom(gathering)
					.where(gathering.user.userId.eq(loginId))//등록한 사람이 로그인 아이디와 일치하는 경우에만.
					.orderBy(gathering.gatheringId.desc())
					.offset(pageRequest.getOffset())
					.limit(pageRequest.getPageSize())
					.fetch();
		} else { // 검색어 있는 경우
		    gatheringList = jpaQueryFactory.selectFrom(gathering)
		            .where(gathering.user.userId.eq(loginId)
		                .and(gathering.title.contains(word))) // 제목에서 검색
		            .orderBy(gathering.gatheringId.desc())
		            .offset(pageRequest.getOffset())
		            .limit(pageRequest.getPageSize())
		            .fetch();
		}
		return gatheringList;
	}
}
