package com.dev.moyering.host.repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.dev.moyering.host.entity.QClassCalendar;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClassCalendarRepositoryImpl implements ClassCalendarRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Map<Integer, Date> findEarliestStartDatesByClassIds(List<Integer> classIds) throws Exception {
		QClassCalendar calendar = QClassCalendar.classCalendar;
		
		List<Tuple> results = jpaQueryFactory
				.select(calendar.hostClass.classId, calendar.startDate.min())
				.from(calendar)
				.where(
					calendar.hostClass.classId.in(classIds),
					calendar.status.eq("모집중"),
					calendar.startDate.goe(Date.valueOf(LocalDate.now()))
				)
				.groupBy(calendar.hostClass.classId)
				.fetch();
		
		return results.stream()
				.collect(Collectors.toMap(
				tuple -> tuple.get(calendar.hostClass.classId), 
				tuple -> tuple.get(calendar.startDate.min())
				));
	}
}
