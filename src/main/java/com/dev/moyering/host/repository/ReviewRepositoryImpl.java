package com.dev.moyering.host.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.dev.moyering.host.entity.QReview;
import com.dev.moyering.host.entity.Review;
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

}
