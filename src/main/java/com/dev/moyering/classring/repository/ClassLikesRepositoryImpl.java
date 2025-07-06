package com.dev.moyering.classring.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.dev.moyering.classring.dto.UtilSearchDto;
import com.dev.moyering.classring.dto.WishlistItemDto;
import com.dev.moyering.classring.entity.QClassLikes;
import com.dev.moyering.host.entity.QClassCalendar;
import com.dev.moyering.host.entity.QHostClass;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import static com.querydsl.core.types.dsl.Expressions.constant;  // 추가

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClassLikesRepositoryImpl implements ClassLikesRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
	public Page<WishlistItemDto> searchByUser(UtilSearchDto dto, Pageable pageable) throws Exception {
        QClassLikes cl = QClassLikes.classLikes;  
        QHostClass hc = QHostClass.hostClass;
        QClassCalendar cc = QClassCalendar.classCalendar;
        QClassCalendar cc2 = new QClassCalendar("cc2");

        // 1) 필터 조건
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(cl.user.userId.eq(dto.getUserId()));
        
        if (dto.getKeywords() != null && !dto.getKeywords().isBlank()) {
            builder.and(hc.name.containsIgnoreCase(dto.getKeywords()));
		} 
        // 2) 서브쿼리: 클래스별 가장 빠른(최소) calendar_id
        var minCalIdSubquery = JPAExpressions
                .select(cc2.calendarId.min())
                .from(cc2)
                .where(cc2.hostClass.eq(hc)        
        		.and(cc2.status.eq("모집중"))
        		);
        
        // 3) 데이터 쿼리: 좋아요 ID, 타입, 클래스명, 설명, 일정, 이미지, 시간, 주소 등
        List<WishlistItemDto> content = jpaQueryFactory
        	    .select(Projections.constructor(
        	        WishlistItemDto.class,
        	        cl.classLikeId,              // 좋아요 ID
        	        constant("class"),           // type (상수 필드)
        	        hc.name,                     // title
        	        cc.startDate,                // date
        	        hc.img1,                     // imageUrl
        	        hc.scheduleStart,            // startTime
        	        hc.scheduleEnd,              // endTime
        	        cl.user.userId,
        	        hc.addr,                     // addr
        	        hc.detailAddr,               // detailAddr
        	        hc.locName,                   // locName
        	        hc.classId
        	    ))
        	    .from(cl)
        	    .join(cl.hostClass, hc)
        	    .join(cc).on(cc.calendarId.eq(minCalIdSubquery))
        	    .where(builder)
        	    .orderBy(cl.classLikeId.desc())
        	    .offset(pageable.getOffset())
        	    .limit(pageable.getPageSize())
        	    .fetch();

        // 4) 전체 개수 카운트
        long total = jpaQueryFactory
            .select(cl.count())
            .from(cl)
            .join(cl.hostClass, hc)
            .join(cc).on(cc.calendarId.eq(minCalIdSubquery))
            .where(builder)
            .fetchOne();

        // 5) Page 객체로 포장하여 반환
        return new PageImpl<>(content, pageable, total);
	}
	

}
