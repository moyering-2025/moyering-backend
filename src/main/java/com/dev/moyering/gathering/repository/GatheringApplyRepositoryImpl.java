package com.dev.moyering.gathering.repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.dev.moyering.gathering.dto.GatheringApplyDto;
import com.dev.moyering.gathering.entity.QGathering;
import com.dev.moyering.gathering.entity.QGatheringApply;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GatheringApplyRepositoryImpl implements GatheringApplyRepositoryCustom {
	
	@Autowired
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<GatheringApplyDto> findApplyUserListByGatheringId(Integer gatheringId) throws Exception {
		QGatheringApply gatheringApply = QGatheringApply.gatheringApply;
//		List<Tuple> results = jpaQueryFactory
//				.select(gatheringApply.user.name, calendar.startDate.min())
//				.from(calendar)
//				.where(
//					calendar.hostClass.classId.in(classIds),
//					calendar.status.eq("모집중"),
//					calendar.startDate.goe(Date.valueOf(LocalDate.now()))
//				)
//				.groupBy(calendar.hostClass.classId)
//				.fetch();
//
//		return results.stream()
//				.collect(Collectors.toMap(
//				tuple -> tuple.get(calendar.hostClass.classId), 
//				tuple -> tuple.get(calendar.startDate.min())
//				));
		return null;
	}
}
