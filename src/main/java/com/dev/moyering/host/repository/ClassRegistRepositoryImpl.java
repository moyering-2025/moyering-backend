package com.dev.moyering.host.repository;

import java.util.List;

import com.dev.moyering.host.entity.ClassRegist;
import com.dev.moyering.host.entity.QClassCalendar;
import com.dev.moyering.host.entity.QClassRegist;
import com.dev.moyering.host.entity.QHost;
import com.dev.moyering.host.entity.QHostClass;
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

}
