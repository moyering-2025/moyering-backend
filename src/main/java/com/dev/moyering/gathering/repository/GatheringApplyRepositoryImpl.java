package com.dev.moyering.gathering.repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.dev.moyering.gathering.dto.GatheringApplyDto;
import com.dev.moyering.gathering.entity.GatheringApply;
import com.dev.moyering.gathering.entity.QGatheringApply;
import com.dev.moyering.gathering.entity.QGatheringInquiry;
import com.dev.moyering.user.entity.QUser;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;

import lombok.RequiredArgsConstructor;
import com.querydsl.core.Tuple;

@RequiredArgsConstructor
public class GatheringApplyRepositoryImpl implements GatheringApplyRepositoryCustom {
	
	@Autowired
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<GatheringApplyDto> findApplyUserListByGatheringId(Integer gatheringId) throws Exception {
	    QGatheringApply gatheringApply = QGatheringApply.gatheringApply;
	    QUser user = QUser.user;
		
	    return jpaQueryFactory
	        .select(Projections.constructor(GatheringApplyDto.class,
	            gatheringApply.gatheringApplyId,
	            gatheringApply.gathering.gatheringId,
	            user.userId,
	            user.nickName,  // DTO에서는 name 필드를 사용
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
	public void updateGatheringApplyApproval(Integer gatheringApplyId, boolean isApproved) throws Exception {
		 QGatheringApply gatheringApply = QGatheringApply.gatheringApply;
		JPAUpdateClause clause = jpaQueryFactory.update(gatheringApply)
				.set(gatheringApply.isApproved, isApproved)
				.where(gatheringApply.gatheringApplyId.eq(gatheringApplyId));
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
	
//	@Override
//	public List<GatheringApplyDto> selectApplicationsByGatheringId(Integer gatheringId) throws Exception {
//	    QGatheringApply gatheringApply = QGatheringApply.gatheringApply;
//	    
//	    List<GatheringApply> applications = jpaQueryFactory.selectFrom(gatheringApply)
//	            .where(gatheringApply.gathering.gatheringId.eq(gatheringId))
//	            .orderBy(gatheringApply.applyDate.desc())
//	            .fetch();
//	    
//	    return applications.stream()
//	            .map(GatheringApply::toDto)
//	            .collect(Collectors.toList());
//	}

}
