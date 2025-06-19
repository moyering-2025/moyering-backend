package com.dev.moyering.gathering.repository;

import java.util.Date;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dev.moyering.gathering.dto.GatheringApplyDto;
import com.dev.moyering.gathering.entity.GatheringApply;
import com.dev.moyering.gathering.entity.QGatheringApply;
import com.dev.moyering.user.entity.QUser;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.core.types.Projections;
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
	public void updateMemberApproval(Integer gatheringId, Integer userId, boolean isApproved) throws Exception {
		 QGatheringApply gatheringApply = QGatheringApply.gatheringApply;
		
	}

	@Override
	public void applyToGathering(GatheringApplyDto gatheringApplyDto) throws Exception {
		 QGatheringApply gatheringApply = QGatheringApply.gatheringApply;
	}
}
