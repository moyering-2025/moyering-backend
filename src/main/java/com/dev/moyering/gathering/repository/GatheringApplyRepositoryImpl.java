package com.dev.moyering.gathering.repository;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import com.dev.moyering.gathering.dto.GatheringApplyDto;
import com.dev.moyering.gathering.dto.GatheringDto;
import com.dev.moyering.gathering.entity.Gathering;
import com.dev.moyering.gathering.entity.GatheringApply;
import com.dev.moyering.gathering.entity.QGathering;
import com.dev.moyering.gathering.entity.QGatheringApply;
import com.dev.moyering.gathering.entity.QGatheringInquiry;
import com.dev.moyering.user.entity.QUser;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPADeleteClause;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;

import lombok.RequiredArgsConstructor;
import com.querydsl.core.Tuple;

@RequiredArgsConstructor
public class GatheringApplyRepositoryImpl implements GatheringApplyRepositoryCustom {
	
	@Autowired
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<GatheringApplyDto> findApprovedUserListByGatheringId(Integer gatheringId) throws Exception {
	    QGatheringApply gatheringApply = QGatheringApply.gatheringApply;
	    QUser user = QUser.user;
		
	    return jpaQueryFactory
	        .select(Projections.constructor(GatheringApplyDto.class,
	            gatheringApply.gatheringApplyId,
	            gatheringApply.gathering.gatheringId,
	            user.userId,
	            user.nickName,  
	            user.profile,
	            user.intro,
	            gatheringApply.applyDate,
	            gatheringApply.isApproved,
	            gatheringApply.aspiration
	        ))
	        .from(gatheringApply)
	        .join(user).on(gatheringApply.user.userId.eq(user.userId))
	        .where(
	            gatheringApply.gathering.gatheringId.eq(gatheringId),
	            gatheringApply.isApproved.isTrue()
	        )
	        .fetch();
	}
	
	@Override
	public Integer findApprovedUserCountByGatheringId(Integer gatheringId) throws Exception {
		QGatheringApply gatheringApply = QGatheringApply.gatheringApply;
		return jpaQueryFactory
			    .select(gatheringApply.count())
			    .from(gatheringApply)
			    .where(gatheringApply.gathering.gatheringId.eq(gatheringId)
			        .and(gatheringApply.isApproved.isTrue()))
			    .fetchOne().intValue();
	}
	@Override
	public Integer findApplyUserCountByGatheringId(Integer gatheringId) throws Exception {
		QGatheringApply gatheringApply = QGatheringApply.gatheringApply;
		return jpaQueryFactory
			    .select(gatheringApply.count())
			    .from(gatheringApply)
			    .where(gatheringApply.gathering.gatheringId.eq(gatheringId))
			    .fetchOne().intValue();
	}
	@Override
	public List<GatheringApplyDto> findApplyUserListByGatheringIdForOrganizer(Integer gatheringId) throws Exception {
		QGatheringApply gatheringApply = QGatheringApply.gatheringApply;
		QUser user = QUser.user;
		
		return jpaQueryFactory
		    .select(Projections.constructor(GatheringApplyDto.class,
		            gatheringApply.gatheringApplyId,
		            gatheringApply.gathering.gatheringId,
		            user.userId,
		            user.nickName,  
		            user.profile,
		            user.intro,
		            gatheringApply.applyDate,
		            gatheringApply.isApproved,
		            gatheringApply.aspiration
		            ))
		    .from(gatheringApply)
		    .join(user).on(gatheringApply.user.userId.eq(user.userId))
		    .where(gatheringApply.gathering.gatheringId.eq(gatheringId))
		    .orderBy(
		        gatheringApply.gathering.gatheringId.asc(),
		        new CaseBuilder()
		            .when(gatheringApply.isApproved.isNull()).then(1)
		            .when(gatheringApply.isApproved.eq(true)).then(2)
		            .when(gatheringApply.isApproved.eq(false)).then(3)
		            .otherwise(4).asc(),
		        gatheringApply.gatheringApplyId.asc()
		    )
		    .fetch();
	}

	@Override
	@Transactional
	public void updateGatheringApplyApproval(Integer gatheringApplyId, boolean isApproved) throws Exception {
	    QGatheringApply gatheringApply = QGatheringApply.gatheringApply;
	    JPAUpdateClause clause = jpaQueryFactory.update(gatheringApply)
	            .set(gatheringApply.isApproved, isApproved)
	            .where(gatheringApply.gatheringApplyId.eq(gatheringApplyId));
//	    java.sql.Date today = java.sql.Date.valueOf(LocalDate.now());
//	    if(isApproved) {
//	        clause.set(gatheringApply.rejectionDate,(Date) null)
//            .set(gatheringApply.approvalDate, today);
//	    } else {
//	        clause.set(gatheringApply.rejectionDate, today);
//	    }
	    clause.execute();
	}

	@Override
	public Integer findByGatheringIdAndUserId(Integer gatheringId, Integer userId) throws Exception {
		QGatheringApply gatheringApply = QGatheringApply.gatheringApply;
	    QUser user = QUser.user;
		return jpaQueryFactory.select(gatheringApply.count())
		        .from(gatheringApply)
		        .join(user).on(gatheringApply.user.userId.eq(user.userId))
		        .where(
		            gatheringApply.gathering.gatheringId.eq(gatheringId),
		            gatheringApply.user.userId.eq(userId)
		        )
		        .fetchOne().intValue();
	}

	@Override
	public List<GatheringApplyDto> getAppliedGatheringList(Integer loginId, String word, String status, PageRequest pageRequest) {
	    QGatheringApply gatheringApply = QGatheringApply.gatheringApply;
	    QGathering gathering = QGathering.gathering;
	    QUser user = QUser.user;
	    
	    List<GatheringApply> gatheringApplyList = null;
	    
	    // 기본 조건: 로그인한 유저가 신청한 모임들
	    BooleanExpression condition = gatheringApply.user.userId.eq(loginId);
	    
	    // 검색어가 있는 경우 모임 제목으로 필터링
	    if (word != null && word.trim().length() > 0) {
	        condition = condition.and(gatheringApply.gathering.title.contains(word));
	    }
	    
	    // 상태 조건 추가
	    BooleanExpression statusCondition = getStatusCondition(gatheringApply, status);
	    if (statusCondition != null) {
	        condition = condition.and(statusCondition);
	    }
	    
	    JPAQuery<GatheringApply> query = jpaQueryFactory
	            .selectFrom(gatheringApply)
	            .join(gatheringApply.gathering, gathering).fetchJoin()
	            .join(gatheringApply.user, user).fetchJoin()
	            .where(condition);
	    
	    // 상태별 정렬 기준 적용
	    if ("거절됨".equals(status)) {
	        // 거절됨 상태: 지원일 최신순
	        query = query.orderBy(
	            gatheringApply.applyDate.desc(),
	            gatheringApply.gatheringApplyId.desc()
	        );
	    } else {
	        // 그 외 상태: D-day 오름차순 (meetingDate + startTime), 같은 날이면 지원일 최신순
	        query = query.orderBy(
	            gathering.meetingDate.asc(),
	            gathering.startTime.asc(),
	            gatheringApply.applyDate.desc(),
	            gatheringApply.gatheringApplyId.desc()
	            
	        );
	    }
	    
	    gatheringApplyList = query
	            .offset(pageRequest.getOffset())
	            .limit(pageRequest.getPageSize())
	            .fetch();
	    
	    return gatheringApplyList.stream()
	            .map(GatheringApply::toDto)
	            .collect(Collectors.toList());
	}

	@Override
	public Long findMyApplyListCount(Integer userId, String word, String status) throws Exception {
	   QGatheringApply gatheringApply = QGatheringApply.gatheringApply;
	    QGathering gathering = QGathering.gathering;
	    QUser user = QUser.user;
	    
	    // 기본 조건: 로그인한 유저가 신청한 모임들
	    BooleanExpression condition = gatheringApply.user.userId.eq(userId);
	    
	    // 검색어가 있는 경우 모임 제목으로 필터링
	    if (word != null && word.trim().length() > 0) {
	        condition = condition.and(gatheringApply.gathering.title.contains(word));
	    }
	    
	    // 상태 조건 추가
	    BooleanExpression statusCondition = getStatusCondition(gatheringApply, status);
	    if (statusCondition != null) {
	        condition = condition.and(statusCondition);
	    }
	    
	    // Count 쿼리 실행
	    Long count = jpaQueryFactory
	            .select(gatheringApply.count())
	            .from(gatheringApply)
	            .join(gatheringApply.gathering, gathering)
	            .join(gatheringApply.user, user)
	            .where(condition)
	            .fetchOne();
	    
	    return count != null ? count : 0L;
	}
	// 상태 필터 조건 분기
	private BooleanExpression getStatusCondition(QGatheringApply gatheringApply, String status) {
	    if (status == null || "전체".equals(status)) {
	        return null;
	    }
	    switch (status) {
	        case "대기중":
	            return gatheringApply.isApproved.isNull(); 
	        case "수락됨":
	            return gatheringApply.isApproved.isTrue(); 
	        case "거절됨":
	            return gatheringApply.isApproved.isFalse()
	                    .or(gatheringApply.gathering.canceled.isTrue());
	        default:
	            return null;
	    }
	}

	@Override
	@Transactional
	 public void cancelGatheringApply(Integer gatheringApplyId) throws Exception {
		 QGatheringApply gatheringApply = QGatheringApply.gatheringApply;
	        
		 long deletedCount = jpaQueryFactory.delete(gatheringApply)
            .where(gatheringApply.gatheringApplyId.eq(gatheringApplyId))
            .execute();
		 if (deletedCount == 0) {
			 throw new Exception("비상~~~!");
		 }
    }
}
