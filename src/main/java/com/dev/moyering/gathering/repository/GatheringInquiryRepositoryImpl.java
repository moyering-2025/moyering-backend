package com.dev.moyering.gathering.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import com.dev.moyering.gathering.dto.GatheringInquiryDto;
import com.dev.moyering.gathering.entity.QGatheringInquiry;
import com.dev.moyering.user.entity.QUser;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GatheringInquiryRepositoryImpl implements GatheringInquiryRepositoryCustom {

	@Autowired
	private final JPAQueryFactory jpaQueryFactory;
	
	public List<GatheringInquiryDto> gatheringInquiryListBygatheringId(Integer gatheringId) throws Exception {
		QGatheringInquiry gatheringInquiry = QGatheringInquiry.gatheringInquiry;
		QUser user = QUser.user;
		
	    return jpaQueryFactory
	        .select(Projections.constructor(GatheringInquiryDto.class,
	        	gatheringInquiry.inquiryId,
	        	gatheringInquiry.gathering.gatheringId,
	            user.userId,  // DTO에서는 name 필드를 사용
	            user.nickName,
	            user.profile,
	            gatheringInquiry.inquiryDate,
	            gatheringInquiry.responseContent,
	            gatheringInquiry.responseDate
	        ))
	        .from(gatheringInquiry)
	        .join(user).on(gatheringInquiry.user.userId.eq(user.userId))
	        .where(
	        	gatheringInquiry.gathering.gatheringId.eq(gatheringId)
	        )
	        .fetch(); 
	}
	@Transactional
	public void responseToGatheringInquiry (GatheringInquiryDto gatheringInquiryDto) throws Exception{
		QGatheringInquiry gatheringInquiry = QGatheringInquiry.gatheringInquiry;
		JPAUpdateClause clause = jpaQueryFactory.update(gatheringInquiry)
				.set(gatheringInquiry.responseContent, gatheringInquiryDto.getInquiryContent())
				.set(gatheringInquiry.responseDate, gatheringInquiryDto.getResponseDate())
				.where(gatheringInquiry.inquiryId.eq(gatheringInquiryDto.getInquiryId()));
		clause.execute();
	}
}
