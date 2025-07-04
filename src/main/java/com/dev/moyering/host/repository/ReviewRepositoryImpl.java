package com.dev.moyering.host.repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import org.threeten.bp.format.DateTimeFormatter;

import com.dev.moyering.host.dto.ReviewSearchRequestDto;
import com.dev.moyering.host.entity.QClassCalendar;
import com.dev.moyering.host.entity.QHost;
import com.dev.moyering.host.entity.QHostClass;
import com.dev.moyering.host.entity.QReview;
import com.dev.moyering.host.entity.Review;
import com.dev.moyering.user.entity.QUser;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Page<Review> findReviewsByHostId(Integer hostId, Pageable pageable) {
		QReview review = QReview.review;
		List<Review> content = jpaQueryFactory.selectFrom(review).where(review.host.hostId.eq(hostId))
				.orderBy(review.reviewDate.desc()).offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();
		Long total = jpaQueryFactory.select(review.count()).from(review).where(review.host.hostId.eq(hostId))
				.fetchOne();
		return new PageImpl<Review>(content, pageable, total);
	}

	@Override
	public Page<Review> getReviewsForHost(ReviewSearchRequestDto requestDto, Pageable pageable) {
		QReview review = QReview.review;
		QClassCalendar calendar = QClassCalendar.classCalendar;
		QHostClass hostClass = QHostClass.hostClass;
		QHost host = QHost.host;
		QUser user = QUser.user;
		
		BooleanBuilder builder = new BooleanBuilder();
		
		builder.and(hostClass.host.hostId.eq(requestDto.getHostId()));
		
		if(requestDto.getSearchQuery() != null && !requestDto.getSearchQuery().isBlank()) {
			if("클래스명".equals(requestDto.getSearchFilter())) {
				builder.and(hostClass.name.containsIgnoreCase(requestDto.getSearchQuery()));
			}else if("학생명".equals(requestDto.getSearchFilter())) {
				builder.and(user.name.containsIgnoreCase(requestDto.getSearchQuery()));
			}
		}
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		if(requestDto.getStartDate() != null && !requestDto.getStartDate().isBlank()) {
			builder.and(review.reviewDate.goe(Date.valueOf(LocalDate.parse(requestDto.getStartDate()))));
		}
		if(requestDto.getEndDate() != null && !requestDto.getEndDate().isBlank()) {
			builder.and(review.reviewDate.loe(Date.valueOf(LocalDate.parse(requestDto.getEndDate()))));
		}
		if(requestDto.getCalendarId() != null) {
			builder.and(calendar.calendarId.eq(requestDto.getCalendarId()));
		}
		// 답변 상태 필터
		if ("답변완료".equals(requestDto.getReplyStatus())) {
			builder.and(review.state.eq(1));
		} else if ("답변대기".equals(requestDto.getReplyStatus())) {
			builder.and(review.state.eq(0));
		}
		
		List<Review> content = jpaQueryFactory.selectFrom(review).join(review.classCalendar,calendar)
				.join(calendar.hostClass,hostClass).join(hostClass.host,host).join(review.user,user)
				.where(builder).orderBy(review.reviewDate.desc()).offset(pageable.getOffset())
				.limit(pageable.getPageSize()).fetch();
		
		long total = jpaQueryFactory.selectFrom(review).join(review.classCalendar,calendar)
				.join(calendar.hostClass,hostClass).join(hostClass.host,host).join(review.user,user)
				.where(builder).fetchCount();
		
		return new PageImpl<>(content,pageable,total);
	}

}
