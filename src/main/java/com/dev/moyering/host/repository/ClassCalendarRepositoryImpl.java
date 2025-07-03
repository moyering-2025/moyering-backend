package com.dev.moyering.host.repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.dev.moyering.host.dto.ClassCalendarDto;
import com.dev.moyering.host.entity.ClassCalendar;
import com.dev.moyering.host.entity.ClassRegist;
import com.dev.moyering.host.entity.QClassCalendar;
import com.dev.moyering.host.entity.QClassRegist;
import com.dev.moyering.host.entity.QHostClass;
import com.dev.moyering.user.entity.User;
import com.querydsl.core.BooleanBuilder;
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

	@Override
	public List<ClassCalendar> findTop4ByDistinctHostClass() throws Exception {
		QClassCalendar cc = QClassCalendar.classCalendar;
		
		return jpaQueryFactory
				.selectFrom(cc)
				.where(cc.status.eq("모집중"))
				.groupBy(cc.hostClass.classId)
				.orderBy(cc.registeredCount.max().desc())
				.limit(4)
				.fetch();
	}

	@Override
	public List<ClassCalendar> findRecommendClassesForUser2(User user) throws Exception {
		QClassCalendar calendar = QClassCalendar.classCalendar;

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
	            		calendar.hostClass.subCategory.subCategoryName.in(preferences)
	            );
	        }
	    }
		List<ClassCalendar> preferred = jpaQueryFactory
				.select(calendar)
				.from(calendar)
				.where(builder)
				.groupBy(calendar.hostClass.classId)
				.orderBy(calendar.startDate.asc())
				.limit(4)
				.fetch();
		
		List<Integer> preferredClassIds = preferred.stream()
			    .map(cc -> cc.getHostClass().getClassId())
			    .collect(Collectors.toList());

			// 2단계: 부족하면 나머지로 채우기
			if (preferred.size() < 4) {
			    List<ClassCalendar> fallback = jpaQueryFactory
			        .selectFrom(calendar)
			        .where(
			        		calendar.status.eq("모집중")
			                .and(calendar.startDate.goe(Date.valueOf(LocalDate.now())))
			                .and(preferredClassIds.isEmpty() ? null : calendar.hostClass.classId.notIn(preferredClassIds))
			        )
			        .orderBy(calendar.startDate.asc())
			        .limit(4 - preferred.size())
			        .fetch();

			    preferred.addAll(fallback);
			}

		return preferred;	
		}

	@Override
	public List<ClassCalendarDto> findAllScheduleWithDetailsByUserId(Integer userId) throws Exception {
	    QClassRegist regist = QClassRegist.classRegist;
	    QClassCalendar calendar = QClassCalendar.classCalendar;
	    QHostClass hostClass = QHostClass.hostClass;
	    
		List<ClassRegist> registList = jpaQueryFactory
			    .select(regist)
			    .from(regist)
			    .join(regist.classCalendar, calendar).fetchJoin()
			    .join(calendar.hostClass, hostClass).fetchJoin()
			    .where(regist.user.userId.eq(userId))
			    .fetch();

		// 그 후 calendar만 뽑아서 DTO 변환
		return registList.stream()
		    .map(r -> r.getClassCalendar().toDto())
		    .collect(Collectors.toList());    
	}
}
