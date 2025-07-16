package com.dev.moyering.host.repository;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.dev.moyering.classring.dto.InquiryResponseDto;
import com.dev.moyering.classring.dto.UtilSearchDto;
import com.dev.moyering.host.dto.InquirySearchRequestDto;
import com.dev.moyering.host.entity.Inquiry;
import com.dev.moyering.host.entity.QClassCalendar;
import com.dev.moyering.host.entity.QHost;
import com.dev.moyering.host.entity.QHostClass;
import com.dev.moyering.host.entity.QInquiry;
import com.dev.moyering.user.entity.QUser;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InquiryRepositoryImpl implements InquiryRepositoryCustom {
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Page<Inquiry> findInquiriesByClassId(Integer classId, Pageable pageable) throws Exception{
		QInquiry inquiry = QInquiry.inquiry;
		List<Inquiry> content = jpaQueryFactory.selectFrom(inquiry)
				.where(inquiry.classCalendar.hostClass.classId.eq(classId)).orderBy(inquiry.inquiryDate.desc())
				.offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();

		Long total = jpaQueryFactory.select(inquiry.count()).from(inquiry)
				.where(inquiry.classCalendar.hostClass.classId.eq(classId)).fetchOne();
		return new PageImpl<Inquiry>(content, pageable, total);
	}


	@Override
	public Page<Inquiry> searchInquiries(InquirySearchRequestDto dto, Pageable pageable) throws Exception {
	    QInquiry inquiry = QInquiry.inquiry;
	    QClassCalendar classCalendar = QClassCalendar.classCalendar;
	    QHostClass hostClass = QHostClass.hostClass;
	    QHost host = QHost.host;
	    QUser user = QUser.user;

	    BooleanBuilder builder = new BooleanBuilder();

//	    builder.and(inquiry.classCalendar.hostClass.host.hostId.eq(dto.getHostId()));
	    builder.and(host.hostId.eq(dto.getHostId()));
	    
	    
	    if(dto.getCalendarId() !=null) {
	    	builder.and(classCalendar.calendarId.eq(dto.getCalendarId()));
	    }
	    if(dto.getHostClassId() != null) {
	    	builder.and(hostClass.classId.eq(dto.getHostClassId()));
	    }

	    // 검색어 필터
	    if (dto.getSearchQuery() != null && !dto.getSearchQuery().isBlank()) {
	        if ("클래스명".equals(dto.getSearchFilter())) {
	            builder.and(hostClass.name.containsIgnoreCase(dto.getSearchQuery()));
	        } else if ("학생명".equals(dto.getSearchFilter())) {
	            builder.and(user.name.containsIgnoreCase(dto.getSearchQuery()));
	        }
	    }

	    // 날짜 필터
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    if (dto.getStartDate() != null && !dto.getStartDate().isBlank()) {
	        builder.and(inquiry.inquiryDate.goe(Date.valueOf(LocalDate.parse(dto.getStartDate(), formatter))));
	    }
	    if (dto.getEndDate() != null && !dto.getEndDate().isBlank()) {
	        builder.and(inquiry.inquiryDate.loe(Date.valueOf(LocalDate.parse(dto.getEndDate(), formatter))));
	    }
	    //답변상태 숫자로 변환
	    
	    // 답변 상태 필터
	    if (dto.getReplyStatus() != null) {
	        if (dto.getReplyStatus() == 1) {
	            builder.and(inquiry.state.eq(1));
	        } else if (dto.getReplyStatus() == 0) {
	            builder.and(inquiry.state.eq(0));
	        }
	    }
	    // 🔥 join 설정
	    List<Inquiry> content = jpaQueryFactory.selectFrom(inquiry)
	            .join(inquiry.classCalendar, classCalendar)
	            .join(classCalendar.hostClass, hostClass)
	            .join(hostClass.host, host)
	            .where(builder)
	            .orderBy(inquiry.state.asc(), inquiry.inquiryDate.desc())
	            .offset(pageable.getOffset())
	            .limit(pageable.getPageSize())
	            .fetch();

	    long total = jpaQueryFactory.selectFrom(inquiry)
	            .join(inquiry.classCalendar, classCalendar)
	            .join(classCalendar.hostClass, hostClass)
	            .join(hostClass.host, host)
	            .where(builder)
	            .fetchCount();

	    return new PageImpl<>(content, pageable, total);
	}



	@Override
	public Page<InquiryResponseDto> findInquriesByUserId(UtilSearchDto dto, Pageable pageable) throws Exception {
		QInquiry iq = QInquiry.inquiry;
	    QClassCalendar cc = QClassCalendar.classCalendar;
	    QHostClass hc = QHostClass.hostClass;
	    QHost h = QHost.host;
	    
	    BooleanBuilder builder = new BooleanBuilder();
	    builder.and(iq.user.userId.eq(dto.getUserId()));
	    
	    if (dto.getStartDate() != null) {
	        builder.and(iq.inquiryDate.goe(dto.getStartDate()));
	    }
	    if (dto.getEndDate() != null) {
	        builder.and(iq.inquiryDate.loe(dto.getEndDate()));
	    }

	    if (dto.getKeywords() != null && !dto.getKeywords().isBlank()) {
	        builder.and(hc.name.containsIgnoreCase(dto.getKeywords()));
	    }


	    if (dto.getTab().equals("pending")) {
	    	builder.and(iq.state.eq(0));
	    } else {
	    	builder.and(iq.state.eq(1));
	    }
	    List<InquiryResponseDto> content = jpaQueryFactory
	    		.select(Projections.constructor(InquiryResponseDto.class,
	    				iq.InquiryId,
	    				iq.content,
	    				iq.inquiryDate,
	    				iq.iqResContent,
	    				iq.responseDate,
	    				hc.name,
	    				h.name,
	    				cc.startDate,
	    				iq.state
	    				))
	    		.from(iq)
	    		.join(iq.classCalendar,cc)
	    		.join(cc.hostClass, hc)
	    		.join(iq.host,h)
	    		.where(builder)
	    		.orderBy(iq.InquiryId.desc())
	    		.offset(pageable.getOffset())
	    		.limit(pageable.getPageSize())
	    		.fetch();
	    
	    Long total = jpaQueryFactory
	    		.select(iq.count())
	    		.from(iq)
	    		.join(iq.classCalendar,cc)
	    		.join(cc.hostClass, hc)
	    		.join(iq.host,h)
	    		.where(builder)
	    		.fetchOne();
	    
		return new PageImpl<InquiryResponseDto>(content,pageable,total);
	}


	@Override
	public List<Inquiry> hostInquiryCount(Integer hostId) {
		QClassCalendar calendar = QClassCalendar.classCalendar;
		QHostClass hostClass = QHostClass.hostClass;
		QHost host = QHost.host;
		QInquiry inquiry = QInquiry.inquiry;
		
		BooleanBuilder builder = new BooleanBuilder();
		builder.and(host.hostId.eq(hostId));
		
		List<Inquiry> list = jpaQueryFactory.selectFrom(inquiry)
				.leftJoin(inquiry.classCalendar,calendar)
				.leftJoin(calendar.hostClass,hostClass)
				.leftJoin(hostClass.host,host)
				.where(builder).fetch();
		
		return list;
	}

}
