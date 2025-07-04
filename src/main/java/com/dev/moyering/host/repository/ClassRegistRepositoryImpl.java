package com.dev.moyering.host.repository;

import java.util.List;
import java.util.stream.Collectors;

import com.dev.moyering.host.dto.CalendarUserDto;
import com.dev.moyering.host.entity.ClassRegist;
import com.dev.moyering.host.entity.QClassCalendar;
import com.dev.moyering.host.entity.QClassRegist;
import com.dev.moyering.host.entity.QHost;
import com.dev.moyering.host.entity.QHostClass;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClassRegistRepositoryImpl implements ClassRegistRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<ClassRegist> findByCalendar_HostClass_Host_HostId(Integer hostId) {
		QClassRegist classRegist = QClassRegist.classRegist;
		QClassCalendar classCalendar = QClassCalendar.classCalendar;
		QHostClass hostClass = QHostClass.hostClass;
		QHost host = QHost.host;

		List<ClassRegist> result = jpaQueryFactory.selectFrom(classRegist)
				.join(classRegist.classCalendar, classCalendar).fetchJoin().join(classCalendar.hostClass, hostClass)
				.fetchJoin().join(hostClass.host, host).fetchJoin().where(host.hostId.eq(hostId)).fetch();
		return result;
	}

	@Override
	public List<CalendarUserDto> findByStudentClass(Integer hostId, Integer userId) {
		
		QClassRegist regist = QClassRegist.classRegist;
		QClassCalendar calendar = QClassCalendar.classCalendar;
		QHostClass hostClass = QHostClass.hostClass;

		List<Tuple> result = jpaQueryFactory
		    .select(
		        calendar.calendarId,
		        hostClass.name,
		        calendar.startDate
		    )
		    .from(regist)
		    .join(regist.classCalendar, calendar)
		    .join(calendar.hostClass, hostClass)
		    .where(
		        hostClass.host.hostId.eq(hostId),
		        regist.user.userId.eq(userId)
		    )
		    .fetch();
		
		return result.stream()
				.map(row -> new CalendarUserDto(
						row.get(calendar.calendarId),
						row.get(hostClass.name),
						row.get(calendar.startDate)))
						.collect(Collectors.toList());
	}

}
