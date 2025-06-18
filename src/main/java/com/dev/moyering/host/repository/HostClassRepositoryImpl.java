package com.dev.moyering.host.repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dev.moyering.host.entity.HostClass;
import com.dev.moyering.host.entity.QClassCalendar;
import com.dev.moyering.host.entity.QHostClass;
import com.dev.moyering.user.entity.User;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HostClassRepositoryImpl implements HostClassRepositoryCustom {
    
	@Autowired
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<HostClass> findRecommendClassesForUser(User user) throws Exception {
		QHostClass hostClass = QHostClass.hostClass;
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
	                hostClass.subCategory.subCategoryName.in(preferences)
	            );
	        }
	    }
		
		return jpaQueryFactory
				.select(hostClass)
				.from(calendar)
				.join(calendar.hostClass,hostClass)
				.where(builder)
				.orderBy(calendar.startDate.asc())
				.limit(4)
				.fetch();
	}
	
}
