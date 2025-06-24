package com.dev.moyering.gathering.repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import com.dev.moyering.gathering.dto.GatheringApplyDto;
import com.dev.moyering.gathering.entity.GatheringApply;
import com.dev.moyering.gathering.entity.QGathering;
import com.dev.moyering.gathering.entity.QGatheringApply;
import com.dev.moyering.gathering.entity.QGatheringLikes;
import com.dev.moyering.user.entity.QUser;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.NonUniqueResultException;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.core.types.Projections;
import lombok.RequiredArgsConstructor;
import com.querydsl.core.Tuple;

@RequiredArgsConstructor
public class GatheringLikesRepositoryImpl implements GatheringLikesRepositoryCustom {
	
	@Autowired
	private final JPAQueryFactory jpaQueryFactory;
	@Override
	public Integer countByGatheringId(Integer gatheringId) throws Exception {
		QGatheringLikes gatheringLikes = QGatheringLikes.gatheringLikes;
		Long res = jpaQueryFactory.select(gatheringLikes.count())
					.where(gatheringLikes.gathering.gatheringId.eq(gatheringId))
					.from(gatheringLikes)
					.fetchOne();
		
		return res.intValue();
	}
	@Override
	public Integer selectGatheringLikes(Integer userId, Integer gatheringId) throws Exception {
		QGatheringLikes gatheringLikes = QGatheringLikes.gatheringLikes;
		return jpaQueryFactory.select(gatheringLikes.gatheringLikeId)
				.from(gatheringLikes)
				.where(gatheringLikes.gathering.gatheringId.eq(gatheringId)
						.and(gatheringLikes.user.userId.eq(userId))
						)
				.fetchOne();
	}
	@Override
	@Transactional
	public void deleteGatheringLikes(Integer gatheringLikeId) throws Exception {
		QGatheringLikes gatheringLikes = QGatheringLikes.gatheringLikes;
		
		jpaQueryFactory.delete(gatheringLikes)
			.where(gatheringLikes.gatheringLikeId.eq(gatheringLikeId)).execute();
	}


}
