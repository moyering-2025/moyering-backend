package com.dev.moyering.host.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import com.dev.moyering.host.dto.ReviewSearchRequestDto;
import com.dev.moyering.host.entity.QClassCalendar;
import com.dev.moyering.host.entity.QHost;
import com.dev.moyering.host.entity.QHostClass;
import com.dev.moyering.host.entity.QReview;
import com.dev.moyering.host.entity.Review;
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
		List<Review> content = jpaQueryFactory
				.selectFrom(review)
				.where(review.host.hostId.eq(hostId))
				.orderBy(review.reviewDate.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();
		Long total = jpaQueryFactory
				.select(review.count())
				.from(review)
				.where(review.host.hostId.eq(hostId))
				.fetchOne();
		return new PageImpl<Review>(content,pageable,total);
	}

	@Override
	public Page<Review> getReviewsForHost(ReviewSearchRequestDto requestDto, Pageable pageable) {
		QReview review = QReview.review;
		QClassCalendar calendar = QClassCalendar.classCalendar;
		QHostClass hostClass = QHostClass.hostClass;
		QHost host = QHost.host;
		
		BooleanBuilder builder = new BooleanBuilder();
		
		builder.and(hostClass.host.hostId.eq(requestDto.getHostId()));
		
		if(StringUtils.hasText(requestDto.getClassName())) {
			builder.and(hostClass.name.containsIgnoreCase(requestDto.getClassName()));
		}
		
		if(StringUtils.hasText(requestDto.getHostName())) {
			builder.and(host.name.containsIgnoreCase(requestDto.getHostName()));
		}
		
		if(StringUtils.hasText(requestDto.getStudentName())) {
			builder.and(review.user.nickName.containsIgnoreCase(requestDto.getStudentName()));
		}
		
		if(requestDto.getStartDate() != null && requestDto.getEndDate() != null) {
			builder.and(review.reviewDate.between(requestDto.getStartDate(), requestDto.getEndDate()));
		}
		
		JPAQuery<Review> content = jpaQueryFactory
				.selectFrom(review)
				.leftJoin(review.classCalendar,calendar)
				.leftJoin(calendar.hostClass,hostClass)
				.leftJoin(hostClass.host,host)
				.where(builder);
		
		long total = content.fetchCount();
		List<Review> reviews = content
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.orderBy(review.reviewDate.desc())
				.fetch();
		
		return new PageImpl<>(reviews,pageable,total);
	}

}
