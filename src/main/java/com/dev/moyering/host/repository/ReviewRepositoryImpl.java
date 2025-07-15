package com.dev.moyering.host.repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.threeten.bp.format.DateTimeFormatter;

import com.dev.moyering.classring.dto.UserReviewResponseDto;
import com.dev.moyering.classring.dto.UtilSearchDto;
import com.dev.moyering.classring.dto.WritableReviewResponseDto;
import com.dev.moyering.host.dto.ReviewDto;
import com.dev.moyering.host.dto.ReviewSearchRequestDto;
import com.dev.moyering.host.entity.QClassCalendar;
import com.dev.moyering.host.entity.QClassRegist;
import com.dev.moyering.host.entity.QHost;
import com.dev.moyering.host.entity.QHostClass;
import com.dev.moyering.host.entity.QReview;
import com.dev.moyering.host.entity.Review;
import com.dev.moyering.user.entity.QUser;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
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
		
		if(requestDto.getCalendarId() != null) {
			builder.and(calendar.calendarId.eq(requestDto.getCalendarId()));
		}
		
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
		if ("1".equals(requestDto.getReplyStatus())) {
			builder.and(review.state.eq(1));
		} else if ("0".equals(requestDto.getReplyStatus())) {
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

	@Override
	public Page<WritableReviewResponseDto> findWritableReviews(UtilSearchDto dto, Pageable pageable) throws Exception {
	    QClassRegist cr = QClassRegist.classRegist;
	    QClassCalendar cc = QClassCalendar.classCalendar;
	    QHostClass hc = QHostClass.hostClass;
	    QReview r = QReview.review;

	    BooleanBuilder builder = new BooleanBuilder();
	    builder.and(cr.user.userId.eq(dto.getUserId()));
	    builder.and(cc.status.eq("종료"));
	    builder.and(r.reviewId.isNull());

	    if (dto.getStartDate() != null) {
	        builder.and(cc.startDate.goe(dto.getStartDate()));
	    }
	    if (dto.getEndDate() != null) {
	        builder.and(cc.startDate.loe(dto.getEndDate()));
	    }

	    List<WritableReviewResponseDto> content = jpaQueryFactory
	        .select(Projections.constructor(WritableReviewResponseDto.class,
	            hc.name,
	            cc.startDate,
	            cr.classCalendar.calendarId,
	            cr.user.userId,
	            hc.host.hostId
	        ))
	        .from(cr)
	        .join(cr.classCalendar, cc)
	        .join(cc.hostClass, hc)
	        .leftJoin(r).on(
	            r.classCalendar.calendarId.eq(cr.classCalendar.calendarId)
	            .and(r.user.userId.eq(cr.user.userId))
	        )
	        .where(builder)
	        .orderBy(cc.startDate.asc())
	        .offset(pageable.getOffset())
	        .limit(pageable.getPageSize())
	        .fetch();

	    Long total = jpaQueryFactory
	        .select(cr.count())
	        .from(cr)
	        .join(cr.classCalendar, cc)
	        .join(cc.hostClass, hc)
	        .leftJoin(r).on(
	            r.classCalendar.calendarId.eq(cr.classCalendar.calendarId)
	            .and(r.user.userId.eq(cr.user.userId))
	        )
	        .where(builder)
	        .fetchOne();

	    return new PageImpl<>(content, pageable, total);
	}

	@Override
	public Page<UserReviewResponseDto> findDoneReviews(UtilSearchDto dto, Pageable pageable) throws Exception {
		    QReview r = QReview.review;
		    QClassCalendar cc = QClassCalendar.classCalendar;
		    QHostClass hc = QHostClass.hostClass;

		    BooleanBuilder builder = new BooleanBuilder();
		    builder.and(r.user.userId.eq(dto.getUserId()));

		    if (dto.getStartDate() != null) {
		        builder.and(r.reviewDate.goe(dto.getStartDate()));
		    }
		    if (dto.getEndDate() != null) {
		        builder.and(r.reviewDate.loe(dto.getEndDate()));
		    }

		    List<UserReviewResponseDto> content = jpaQueryFactory
		    	    .select(Projections.constructor(UserReviewResponseDto.class,
		    	        r.reviewId,
		    	        hc.name,
		    	        cc.startDate,
		    	        r.reviewDate,
		    	        r.star,
		    	        r.content,
		    	        r.revRegCotnent,
		    	        r.responseDate,
		    	        r.reviewImg
		    	    ))
		        .from(r)
		        .join(r.classCalendar, cc)
		        .join(cc.hostClass, hc)
		        .where(builder)
		        .orderBy(r.reviewId.desc())
		        .offset(pageable.getOffset())
		        .limit(pageable.getPageSize())
		        .fetch();

		    Long total = jpaQueryFactory
		        .select(r.count())
		        .from(r)
		        .join(r.classCalendar, cc)
		        .join(cc.hostClass, hc)
		        .where(builder)
		        .fetchOne();

		    return new PageImpl<>(content, pageable, total);

	}

	@Override
	public List<Review> findTop3ByClassId(Integer classId) throws Exception {
        QReview review = QReview.review;
        QClassCalendar calendar = QClassCalendar.classCalendar;
        QHostClass hostClass = QHostClass.hostClass;

        return jpaQueryFactory
            .selectFrom(review)
            .join(review.classCalendar, calendar)
            .join(calendar.hostClass, hostClass)
            .where(hostClass.classId.eq(classId))
            .orderBy(review.reviewDate.desc())
            .limit(3)
            .fetch();	
        }

	@Override
	public Page<Review> findReviewsByClassId(Integer classId, Pageable pageable) throws Exception {
 
		QReview review = QReview.review;
		    QClassCalendar calendar = QClassCalendar.classCalendar;
		    QHostClass hostClass = QHostClass.hostClass;
		    
		    
		    

		    List<Review> content = jpaQueryFactory
		            .selectFrom(review)
		            .join(review.classCalendar, calendar)
		            .join(calendar.hostClass, hostClass)
		            .where(hostClass.classId.eq(classId))
		            .orderBy(review.reviewDate.desc())
		            .offset(pageable.getOffset())
		            .limit(pageable.getPageSize())
		            .fetch();

		    Long total = jpaQueryFactory
		            .select(review.count())
		            .from(review)
		            .join(review.classCalendar, calendar)
		            .join(calendar.hostClass, hostClass)
		            .where(hostClass.classId.eq(classId))
		            .fetchOne();

		    return new PageImpl<>(content, pageable, total == null ? 0 : total);		
	}

}
