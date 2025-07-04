package com.dev.moyering.host.repository;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.dev.moyering.host.dto.InquirySearchRequestDto;
import com.dev.moyering.host.entity.Inquiry;
import com.dev.moyering.host.entity.QClassCalendar;
import com.dev.moyering.host.entity.QHost;
import com.dev.moyering.host.entity.QHostClass;
import com.dev.moyering.host.entity.QInquiry;
import com.dev.moyering.user.entity.QUser;
import com.querydsl.core.BooleanBuilder;
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
		QUser user = QUser.user; // 학생명 검색 시 필요

		BooleanBuilder builder = new BooleanBuilder();

		// 🔥 join 필수 (null 방지)
		builder.and(host.hostId.eq(dto.getHostId()));

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
		if(dto.getHostClassId()!=null) {
			builder.and(hostClass.classId.eq(dto.getHostClassId()));
		}
		if(dto.getCalendarId()!=null) {
			builder.and(classCalendar.calendarId.eq(dto.getCalendarId()));
		}

		// 답변 상태 필터
		if ("답변완료".equals(dto.getReplyStatus())) {
			builder.and(inquiry.state.eq(1));
		} else if ("답변대기".equals(dto.getReplyStatus())) {
			builder.and(inquiry.state.eq(0));
		}

		// 🔥 join 설정
		List<Inquiry> content = jpaQueryFactory.selectFrom(inquiry).join(inquiry.classCalendar, classCalendar)
				.join(classCalendar.hostClass, hostClass).join(hostClass.host, host).join(inquiry.user, user)
				.where(builder).orderBy(inquiry.inquiryDate.desc()).offset(pageable.getOffset())
				.limit(pageable.getPageSize()).fetch();

		long total = jpaQueryFactory.selectFrom(inquiry).join(inquiry.classCalendar, classCalendar)
				.join(classCalendar.hostClass, hostClass).join(hostClass.host, host).join(inquiry.user, user)
				.where(builder).fetchCount();

		return new PageImpl<>(content, pageable, total);
	}

}
