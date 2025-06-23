package com.dev.moyering.host.repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.dev.moyering.admin.dto.AdminClassDto;
import com.dev.moyering.admin.dto.AdminClassSearchCond;
import com.dev.moyering.host.entity.*;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.domain.Pageable;

import com.dev.moyering.user.entity.User;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.dev.moyering.common.entity.QSubCategory.subCategory;
import static com.dev.moyering.host.entity.QClassCalendar.classCalendar;
import static com.dev.moyering.host.entity.QHost.host;
import static com.dev.moyering.host.entity.QHostClass.hostClass;

@Repository
@RequiredArgsConstructor
public class HostClassRepositoryImpl implements HostClassRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<HostClass> findRecommendClassesForUser(User user) throws Exception {
		QHostClass hostClass = QHostClass.hostClass;
		QClassCalendar calendar = classCalendar;
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

	@Override
	public List<AdminClassDto> searchClassForAdmin(AdminClassSearchCond cond, Pageable pageable) throws Exception {
		List<AdminClassDto> content = jpaQueryFactory
				// SQL 조회 시점에 DTO 생성자에 직접 넣어주기
//		Projections.constructor() :  Java의 리플렉션(reflection) 을 사용해서 new DTO(값, 값, 값) 형태로 생성자 자체를 호출
				.select(Projections.constructor(AdminClassDto.class,
						hostClass.classId,
						hostClass.subCategory,
						hostClass.host.hostId,
						hostClass.host.name,
						hostClass.price,
						hostClass.recruitMin,
						hostClass.recruitMax,
						hostClass.regDate,
						classCalendar.status
				))
				.from(hostClass)
				.leftJoin(hostClass.host, host) // 강사 조인
				.leftJoin(hostClass.subCategory, subCategory) // 서브 카테고리 조인
				.leftJoin(classCalendar)
				.on(hostClass.classId.eq(classCalendar.hostClass.classId))
				.where(
						searchKeyword(cond.getKeyword()), // 키워드 검색 조건
						filterByStatus(cond.getStatusFilter()), // 상태 필터 조건
						filterByCategory(cond.getCategory()), //카테고리 필터 조건
						filterByDateRange(cond.getFromDate(), cond.getToDate()) //날짜 범위 조정
				).
				orderBy(hostClass.regDate.desc() // 최신 등록일 순 정렬
				)
				.offset(pageable.getOffset()) // 몇번째 데이터부터 가져올지
				.limit(pageable.getPageSize()) //몇개 가져올지
				.fetch(); // 결과 list 반환
		return content;
	}



	@Override
	public Long countClasses(AdminClassSearchCond cond) throws Exception {
		Long total = jpaQueryFactory
				.select(hostClass.count())
				.from(hostClass)
				.leftJoin(hostClass.host, host)
				.leftJoin(hostClass.subCategory, subCategory)
				.leftJoin(classCalendar)
				.on(hostClass.classId.eq(classCalendar.hostClass.classId))
				.where(
						searchKeyword(cond.getKeyword()), // 키워드 검색 조건
						filterByStatus(cond.getStatusFilter()), // 상태 필터 조건
						filterByCategory(cond.getCategory()), //카테고리 필터 조건
						filterByDateRange(cond.getFromDate(), cond.getToDate()) //날짜 범위 조정
				)
				.fetchOne();
		return total != null ? total : 0L;
	}

	@Override
	public AdminClassDto findClassByClassId(Integer classId) {
		return jpaQueryFactory
				.select(Projections.constructor(AdminClassDto.class,
						hostClass.classId,
						hostClass.subCategory,
						hostClass.host,
						hostClass.host.name,
						hostClass.price,
						hostClass.recruitMin,
						hostClass.recruitMax,
						hostClass.regDate,
						classCalendar.status.coalesce("대기")
				))
				.from(hostClass)
				.leftJoin(hostClass.host, host)
				.leftJoin(hostClass.subCategory, subCategory)
				.leftJoin(classCalendar)
				.on(hostClass.classId.eq(classCalendar.hostClass.classId))
				.where(
						hostClass.classId.eq(classId)
				)
				.fetchOne(); // 단건 조회
	}

	// 키워드 검색 (클래스명, 강사명에서 검색)
	private BooleanExpression searchKeyword(String keyword) {
		if (keyword == null || keyword.trim().isEmpty()) {
			return null; // 검색어 없으면 전체 조회
		}
		// 검색어가 있으면 클래스명 또는 강사명에 포함된 데이터 찾기
		return hostClass.name.containsIgnoreCase(keyword)        // 클래스명에 검색어 포함 (대소문자 무시)
				.or(hostClass.host.name.containsIgnoreCase(keyword)); // 또는 강사명에 검색어 포함
	}

	// 상태 필터링
	private BooleanExpression filterByStatus(String statusFilter) {
		if (statusFilter == null || statusFilter.trim().isEmpty()) {
			return null; // 상태 필터 없으면 전체 조회
		}
		return classCalendar.status.eq(statusFilter); // 해당 상태와 일치하는 데이터
	}

	// 카테고리 필터링
	private BooleanExpression filterByCategory(String category) {
		if (category == null || category.trim().isEmpty()) {
			return null; // 카테고리 필터 없으면 전체 조회
		}
		return subCategory.subCategoryName.containsIgnoreCase(category); // 서브카테고리명에 포함된 데이터
	}

	// 날짜 범위 필터링
	private BooleanExpression filterByDateRange(Date fromDate, Date toDate) {
		if (fromDate == null && toDate == null) {
			return null; // 날짜 조건 없으면 전체 조회
		}
		BooleanExpression condition = null;

		if (fromDate != null) {
			condition = hostClass.regDate.goe(fromDate); // 시작일 이후 데이터
		}
		if (toDate != null) {
			BooleanExpression endCondition = hostClass.regDate.loe(toDate); // 종료일 이전 데이터
			condition = (condition != null) ? condition.and(endCondition) : endCondition;
		}
		return condition;
	}
}


