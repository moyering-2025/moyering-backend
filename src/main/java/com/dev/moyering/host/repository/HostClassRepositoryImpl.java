package com.dev.moyering.host.repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.dev.moyering.admin.dto.AdminClassDto;
import com.dev.moyering.admin.dto.AdminClassSearchCond;
import com.dev.moyering.host.dto.HostClassDto;
import com.dev.moyering.host.entity.*;
import com.dev.moyering.user.entity.QUser;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;


import com.dev.moyering.host.dto.ClassCalendarDto;
import com.dev.moyering.host.entity.ClassCalendar;
import com.dev.moyering.host.entity.HostClass;
import com.dev.moyering.host.entity.QClassCalendar;
import com.dev.moyering.host.entity.QHostClass;
import com.dev.moyering.user.entity.User;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.dev.moyering.common.entity.QSubCategory.subCategory;
import static com.dev.moyering.host.entity.QClassCalendar.classCalendar;
import static com.dev.moyering.host.entity.QHost.host;
import static com.dev.moyering.host.entity.QHostClass.hostClass;
import static com.dev.moyering.user.entity.QUser.user;
import static org.springframework.data.relational.core.sql.Functions.count;

@Repository
@RequiredArgsConstructor
public class HostClassRepositoryImpl implements HostClassRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<HostClass> findRecommendClassesForUser(User user) throws Exception {
		QHostClass hostClass = QHostClass.hostClass;
		QClassCalendar calendar = QClassCalendar.classCalendar;
		QHost host = QHost.host;

		BooleanBuilder builder = new BooleanBuilder();
		builder.and(calendar.status.eq("모집중"));
		builder.and(calendar.startDate.goe(Date.valueOf(LocalDate.now())));

		if (user != null) {
			List<String> preferences = Stream.of(
					user.getCategory1(), user.getCategory2(),
					user.getCategory3(), user.getCategory4(), user.getCategory5()
			).filter(Objects::nonNull).collect(Collectors.toList());

			if (!preferences.isEmpty()) {
				builder.and(
						hostClass.subCategory.subCategoryName.in(preferences)
				);
			}
		}

		return jpaQueryFactory
				.select(hostClass)
				.from(calendar)
				.join(calendar.hostClass, hostClass)
				.where(builder)
				.orderBy(calendar.startDate.asc())
				.limit(4)
				.fetch();
	}

	public Map<Integer, List<ClassCalendarDto>> findHostClassWithCalendar(Integer hostId) {
		QHostClass hostClass = QHostClass.hostClass;
		QClassCalendar calendar = QClassCalendar.classCalendar;

		List<Tuple> results = jpaQueryFactory
				.select(hostClass.classId, calendar)
				.from(hostClass)
				.join(calendar).on(calendar.hostClass.classId.eq(hostClass.classId))
				.where(hostClass.host.hostId.eq(hostId))
				.fetch();

		Map<Integer, List<ClassCalendarDto>> resultMap = new HashMap<>();
		for (Tuple tuple : results) {
			Integer classId = tuple.get(hostClass.classId);
			ClassCalendar calendarEntity = tuple.get(calendar);

			ClassCalendarDto calendarDto = calendarEntity.toDto();

			resultMap.computeIfAbsent(classId, k -> new ArrayList<>()).add(calendarDto);
		}
		return resultMap;
	}

	// 관리자 페이지 > 클래스 관리 검색
	@Override
	public Page<AdminClassDto> searchClassForAdmin(AdminClassSearchCond cond, Pageable pageable) throws Exception {
		List<AdminClassDto> content = jpaQueryFactory
				.select(Projections.constructor(AdminClassDto.class,
						user.username,
						hostClass.classId,
						hostClass.subCategory.firstCategory,
						hostClass.subCategory,
						hostClass.host.name,
						hostClass.name,
						hostClass.price,
						hostClass.recruitMin,
						hostClass.recruitMax,
						hostClass.regDate,
						classCalendar.status))
				.from(hostClass)
				.leftJoin(classCalendar).on(hostClass.classId.eq(classCalendar.hostClass.classId))
				.leftJoin(user).on(hostClass.host.userId.eq(user.userId))
				.where(
						likeClassname(cond.getKeyword()),
						eqClassStatus(cond.getStatusFilter()),
						betweenDate(cond.getFromDate(), cond.getToDate())
				)
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();

		Long total = countClasses(cond);

		return new PageImpl<>(content, pageable, total);
	}

	// 관리자 페이지 > 클래스 관리 > 개수 조회
	@Override
	public Long countClasses(AdminClassSearchCond cond) throws Exception {
		return jpaQueryFactory
				.select(hostClass.count())
				.from(hostClass)
				.leftJoin(classCalendar).on(hostClass.classId.eq(classCalendar.hostClass.classId))
				.where(
						likeClassname(cond.getKeyword()),
						eqClassStatus(cond.getStatusFilter()),
						betweenDate(cond.getFromDate(), cond.getToDate())
				)
				.fetchOne();
	}

	// 클래스 검색
	private BooleanExpression likeClassname(String keyword) {
		if (keyword == null || keyword.isEmpty()) return null;

		return hostClass.name.containsIgnoreCase(keyword)
				.or(hostClass.host.name.containsIgnoreCase(keyword))
				.or(user.username.containsIgnoreCase(keyword));
	}

	// 클래스 상태
	private BooleanExpression eqClassStatus(String status) {
		return (status == null || status.isEmpty()) ? null : classCalendar.status.eq(status);
	}

	// 개설일자
	private BooleanExpression betweenDate(Date fromDate, Date toDate) {
		if (fromDate == null || toDate == null) return null;
		return hostClass.regDate.between(fromDate, toDate);
	}
}