package com.dev.moyering.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.dev.moyering.dto.user.GatheringDto;
import com.dev.moyering.entity.user.Gathering;
import com.dev.moyering.entity.user.QGathering;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;

@Repository
public class GatheringRepository {

	@Autowired
	private JPAQueryFactory jpaQueryFactory;
	
	public Gathering selectGathering(Integer gatheringId) throws Exception{
		QGathering gathering = QGathering.gathering;
		return jpaQueryFactory.selectFrom(gathering)
				.where(gathering.gatheringId.eq(gatheringId))
				.fetchOne();
	}
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
		JPAUpdateClause clause = jpaQueryFactory.update(gathering)
				.set(gathering.title, gatheringDto.getTitle())
				.set(gathering.thumbnail, gatheringDto.getThumbnail())
				.set(gathering.gatheringContent, gatheringDto.getGatheringContent())
				.set(gathering.tags, gatheringDto.getAddress())
				.set(gathering.subCategory.subCategoryId, gatheringDto.getSubCategoryId())
				.set(gathering.meetingDate, gatheringDto.getMeetingDate())
				.set(gathering.address, gatheringDto.getAddress())
				.set(gathering.detailAddress, gatheringDto.getDetailAddress())
				.set(gathering.preparationItems, gatheringDto.getPreparationItems())
				.set(gathering.intrOnln, gatheringDto.getIntrOnln())//한줄 소개
				.set(gathering.minAttendees, gatheringDto.getMinAttendees())
				.set(gathering.maxAttendees, gatheringDto.getMaxAttendees())
				.set(gathering.startTime, gatheringDto.getStartTime())
				.set(gathering.endTime, gatheringDto.getEndTime())
				.set(gathering.applyDeadline, gatheringDto.getApplyDeadline())
				.where(gathering.gatheringId.eq(gatheringDto.getGatheringId()));
		clause.execute();
	}
	public void updateGatheringStatus(Integer gatheringId, String status) throws Exception{
		QGathering gathering = QGathering.gathering;
		JPAUpdateClause clause = jpaQueryFactory.update(gathering)
				.set(gathering.status, status)
				.where(gathering.gatheringId.eq(gatheringId));
		clause.execute();
		
	}
	
}
