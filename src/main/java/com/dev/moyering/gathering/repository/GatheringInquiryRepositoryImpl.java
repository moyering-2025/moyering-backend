package com.dev.moyering.gathering.repository;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.dev.moyering.gathering.dto.GatheringInquiryDto;
import com.dev.moyering.gathering.entity.GatheringInquiry;
import com.dev.moyering.gathering.entity.QGatheringInquiry;
import com.dev.moyering.user.entity.QUser;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GatheringInquiryRepositoryImpl implements GatheringInquiryRepositoryCustom {

	@Autowired
	private final JPAQueryFactory jpaQueryFactory;
	@Autowired
	private ModelMapper modelMapper;
	
	public List<GatheringInquiryDto> gatheringInquiryListBygatheringId(Integer gatheringId) throws Exception {
	    QGatheringInquiry gatheringInquiry = QGatheringInquiry.gatheringInquiry;
	    
	    List<GatheringInquiry> inquiryList = jpaQueryFactory
	        .selectFrom(gatheringInquiry)
	        .leftJoin(gatheringInquiry.user).fetchJoin()  // User 정보를 함께 조회
	        .leftJoin(gatheringInquiry.gathering).fetchJoin()  // Gathering 정보를 함께 조회
	        .where(gatheringInquiry.gathering.gatheringId.eq(gatheringId))
	        .orderBy(gatheringInquiry.inquiryId.desc())
	        .fetch();
	        
	    return inquiryList.stream()
	        .map(GatheringInquiry::toDto)  // 엔티티의 toDto() 메소드 활용
	        .collect(Collectors.toList());
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
