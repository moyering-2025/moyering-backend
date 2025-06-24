package com.dev.moyering.host.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.dev.moyering.host.entity.Inquiry;
import com.dev.moyering.host.entity.QInquiry;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InquiryRepositoryImpl implements InquiryRepositoryCustom {
	private final JPAQueryFactory jpaQueryFactory;
	
	@Override
	public Page<Inquiry> findInquiriesByClassId(Integer classId, Pageable pageable) {
		QInquiry inquiry = QInquiry.inquiry;
		List<Inquiry> content = jpaQueryFactory
				.selectFrom(inquiry)
				.where(inquiry.classCalendar.hostClass.classId.eq(classId))
				.orderBy(inquiry.inquiryDate.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();
		
		Long total = jpaQueryFactory
				.select(inquiry.count())
				.from(inquiry)
				.where(inquiry.classCalendar.hostClass.classId.eq(classId))
				.fetchOne();
		return new PageImpl<Inquiry>(content,pageable,total);
	}

}
