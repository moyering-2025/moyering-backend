package com.dev.moyering.host.repository;

import static com.dev.moyering.host.entity.QClassCalendar.classCalendar;
import static com.dev.moyering.host.entity.QHostClass.hostClass;
import static com.dev.moyering.user.entity.QUser.user;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.querydsl.core.types.dsl.Expressions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.dev.moyering.admin.dto.AdminClassDto;
import com.dev.moyering.admin.dto.AdminClassSearchCond;
import com.dev.moyering.common.dto.MainSearchRequestDto;
import com.dev.moyering.common.entity.QSubCategory;
import com.dev.moyering.host.dto.ClassCalendarDto;
import com.dev.moyering.host.dto.HostClassDto;
import com.dev.moyering.host.dto.StudentSearchRequestDto;
import com.dev.moyering.host.entity.ClassCalendar;
import com.dev.moyering.host.entity.HostClass;
import com.dev.moyering.host.entity.QClassCalendar;
import com.dev.moyering.host.entity.QClassRegist;
import com.dev.moyering.host.entity.QHost;
import com.dev.moyering.host.entity.QHostClass;
import com.dev.moyering.user.entity.QUser;
import com.dev.moyering.user.entity.User;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

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
						hostClass.classId,
						hostClass.subCategory.firstCategory.categoryName,
						hostClass.subCategory.subCategoryName,
						hostClass.host.userId,
						user.username,
						hostClass.host.name,
						hostClass.name,
						hostClass.price,
						hostClass.recruitMin,
						hostClass.recruitMax,
						hostClass.regDate,
						classCalendar.status
				))
				.from(classCalendar)  // classCalendar를 메인으로
				.join(classCalendar.hostClass, hostClass)
				.leftJoin(user).on(hostClass.host.userId.eq(user.userId))
				.where(
						likeHostUserNameOrNameOrClassname(cond.getKeyword()),
						eqClassStatus(cond.getStatusFilter()),
						betweenDate(cond.getFromDate(), cond.getToDate())
				)
						.orderBy(

		Expressions.cases()
				.when(classCalendar.status.eq("승인대기")).then(0)  // 승인대기가 먼저
				.when(classCalendar.status.eq("모집중")).then(1)
				.otherwise(3).asc(),                              // 나머지는 마지막
				hostClass.regDate.desc())

				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();

		Long total = countClasses(cond);
		return new PageImpl<>(content, pageable, total);
	}

	@Override
	public Long countClasses(AdminClassSearchCond cond) throws Exception {
		return jpaQueryFactory
				.select(classCalendar.count())
				.from(classCalendar)  // classCalendar를 메인으로
				.join(classCalendar.hostClass, hostClass)  // INNER JOIN
				.leftJoin(user).on(hostClass.host.userId.eq(user.userId))
				.where(
						likeHostUserNameOrNameOrClassname(cond.getKeyword()),
						eqClassStatus(cond.getStatusFilter()),
						betweenDate(cond.getFromDate(), cond.getToDate())
				)
				.fetchOne();
	}
	// 클래스 검색
	private BooleanExpression likeHostUserNameOrNameOrClassname(String keyword) {
		if (keyword == null || keyword.isEmpty()) return null; // 검색어 없으면 전체 조회

		// 대소문자 구분없이 문자열 포함해서 검색
		return hostClass.name.containsIgnoreCase(keyword) // 클래스명
				.or(hostClass.host.name.containsIgnoreCase(keyword)) // 강사 이름
				.or(user.username.containsIgnoreCase(keyword)); // 강사 로그인 아이디
	}

	// 클래스 상태
	private BooleanExpression eqClassStatus(List<String> statusFilter) {
		if (statusFilter == null || statusFilter.isEmpty()) return null;
		return classCalendar.status.in(statusFilter);
	}

	// 개설일자
	private BooleanExpression betweenDate(Date fromDate, Date toDate) {
		if (fromDate == null || toDate == null) return null;
		return hostClass.regDate.between(fromDate, toDate);
	}

	@Override
	public Page<User> searchClassStudent(StudentSearchRequestDto dto, Pageable pageable) throws Exception {
		QUser user = QUser.user;
		QHostClass hostClass = QHostClass.hostClass;
		QClassCalendar calendar = QClassCalendar.classCalendar;
		QHost host = QHost.host;
		QClassRegist regist = QClassRegist.classRegist;

		BooleanBuilder builder = new BooleanBuilder();

		builder.and(host.hostId.eq(dto.getHostId()));

		if (dto.getKeyword() != null && !dto.getKeyword().isBlank()) {
			builder.and(user.name.containsIgnoreCase(dto.getKeyword()));
		}

		List<User> content = jpaQueryFactory
				.select(user).distinct()
				.from(regist)
				.leftJoin(regist.user, user)
				.leftJoin(regist.classCalendar, calendar)
				.leftJoin(calendar.hostClass, hostClass)
				.leftJoin(hostClass.host, host)
				.where(builder)
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();

		long totla = jpaQueryFactory.select(user).from(regist)
				.leftJoin(regist.user, user)
				.leftJoin(regist.classCalendar, calendar)
				.leftJoin(calendar.hostClass, hostClass)
				.leftJoin(hostClass.host, host)
				.where(builder)
				.fetchCount();
		return new PageImpl<>(content, pageable, totla);
	}


	@Override
	public Page<HostClass> findSearchClass(MainSearchRequestDto dto,Pageable pageable) throws Exception {
		QHostClass hostClass= QHostClass.hostClass;
		
		BooleanBuilder builder = new BooleanBuilder();
		builder.or(hostClass.name.containsIgnoreCase(dto.getSearchQuery()));
		builder.or(hostClass.detailDescription.containsIgnoreCase(dto.getSearchQuery()));
		
		List<HostClass> content = jpaQueryFactory.selectFrom(hostClass)
				.where(builder)
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();
		
		long total = jpaQueryFactory.selectFrom(hostClass).where(builder).fetchCount();
		
		return new PageImpl<>(content,pageable,total);
	}

	public List<HostClassDto> findRecommendClassesInDetail(Integer subCategoryId, Integer categoryId, Integer classId) throws Exception {
		QHostClass hc = QHostClass.hostClass;
		QClassCalendar cc = QClassCalendar.classCalendar;
		QSubCategory sc = QSubCategory.subCategory;
		List<HostClass> firstList = jpaQueryFactory
				.selectFrom(hc)
				.where(
						hc.subCategory.subCategoryId.eq(subCategoryId),
						hc.classId.ne(classId),
						JPAExpressions.selectOne()
								.from(cc)
								.where(cc.hostClass.classId.eq(hc.classId)
										.and(cc.status.eq("모집중")))
								.exists()
				).limit(3)
				.fetch();

		if (firstList.size() < 3) {
			int remain = 3 - firstList.size();
			List<Integer> excludeIds = firstList.stream()
					.map(HostClass::getClassId)
					.collect(Collectors.toList());

			List<HostClass> secondList = jpaQueryFactory
					.selectFrom(hc)
					.where(
							hc.subCategory.firstCategory.categoryId.eq(categoryId),
							hc.subCategory.subCategoryId.ne(subCategoryId),
							hc.classId.notIn(excludeIds),
							JPAExpressions.selectOne()
									.from(cc)
									.where(cc.hostClass.classId.eq(hc.classId)
											.and(cc.status.eq("모집중")))
									.exists()
					)
					.limit(remain)
					.fetch();

			firstList.addAll(secondList);
		}
		return firstList.stream().map(h -> h.toDto()).collect(Collectors.toList());
	}

	@Override
	public int updateClassStatus(Integer classId) throws Exception {
		List<ClassCalendar> calendars = jpaQueryFactory
				.selectFrom(classCalendar)
				.where(
						classCalendar.hostClass.classId.eq(classId)
								.and(classCalendar.status.eq("승인대기"))
				)
				.fetch();

		if (calendars.isEmpty()) {
			return 0;
		}

		// 모든 캘린더 상태를 "모집중"으로 변경
		for (ClassCalendar calendar : calendars) {
			calendar.changeStatus("모집중");
		}

		return calendars.size();
	}
	
	@Override
	   public List<ClassCalendar> findByHostId(Integer hostId) {
	      QHost host = QHost.host;
	      QHostClass hostClass = QHostClass.hostClass;
	      QClassCalendar calendar = QClassCalendar.classCalendar;
	      
	      BooleanBuilder builder = new BooleanBuilder();
	      builder.and(host.hostId.eq(hostId));
	      
	      List<ClassCalendar> list = jpaQueryFactory.selectFrom(calendar)
	            .leftJoin(calendar.hostClass,hostClass)
	            .leftJoin(hostClass.host,host)
	            .where(builder).fetch();
	      
	      return list;
	   }
}