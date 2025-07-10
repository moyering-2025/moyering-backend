package com.dev.moyering.gathering.repository;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import com.dev.moyering.gathering.dto.GatheringInquiryDto;
import com.dev.moyering.gathering.entity.GatheringInquiry;
import com.dev.moyering.gathering.entity.QGathering;
import com.dev.moyering.gathering.entity.QGatheringInquiry;
import com.dev.moyering.user.entity.QUser;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GatheringInquiryRepositoryImpl implements GatheringInquiryRepositoryCustom {

	@Autowired
	private final JPAQueryFactory jpaQueryFactory;
	@Override
	public List<GatheringInquiryDto> findInquiriesSentByUser(
	        Integer userId,
	        Date startDate,
	        Date endDate,
	        Boolean isAnswered,
	        PageRequest pageRequest
	) throws Exception {
	    QGatheringInquiry inquiry = QGatheringInquiry.gatheringInquiry;
	    QGathering gathering = QGathering.gathering;
	    QUser inquiryUser = new QUser("inquiryUser");  // 문의자
	    QUser organizerUser = new QUser("organizerUser");  // 주최자
	    
	    // 기본 조건 (사용자 ID) - 항상 적용
	    BooleanBuilder whereCondition = new BooleanBuilder();
	    whereCondition.and(inquiry.user.userId.eq(userId));
	    
	    BooleanBuilder optionalConditions = buildCommonInquiryConditions(startDate, endDate, isAnswered);
	    
	    if (optionalConditions.hasValue()) {
	        whereCondition.and(optionalConditions);
	    }
	    
	    List<GatheringInquiry> entities = jpaQueryFactory
	        .selectFrom(inquiry)
	        .join(inquiry.gathering, gathering).fetchJoin()
	        .join(inquiry.user, inquiryUser).fetchJoin()  // 문의자 정보
	        .join(gathering.user, organizerUser).fetchJoin()  // 주최자 정보
	        .where(whereCondition)  // 통합된 조건 적용
	        .orderBy(inquiry.inquiryDate.desc())
	        .offset(pageRequest.getOffset())
	        .limit(pageRequest.getPageSize())
	        .fetch();
	    return entities.stream()
	        .map(GatheringInquiry::toDto)
	        .collect(Collectors.toList());
	}
	private BooleanBuilder buildCommonInquiryConditions(
	        Date startDate,
	        Date endDate,
	        Boolean isAnswered
	) {
	    QGatheringInquiry inquiry = QGatheringInquiry.gatheringInquiry;
	    BooleanBuilder builder = new BooleanBuilder();
	    
	    // 1. 날짜 범위 조건
	    if (startDate != null) {
	        builder.and(inquiry.inquiryDate.goe(startDate));
	    }
	    if (endDate != null) {
	        builder.and(inquiry.inquiryDate.loe(endDate));
	    }
	    // 2. 답변 상태 조건
	    if (isAnswered != null) {
	        if (isAnswered) {
	            // 답변완료: response_content가 null이 아니고 빈 문자열이 아님
	            BooleanBuilder answeredCondition = new BooleanBuilder();
	            answeredCondition.and(inquiry.responseContent.isNotNull());
	            answeredCondition.and(inquiry.responseContent.ne(""));
	            answeredCondition.and(inquiry.responseContent.trim().ne("")); // 공백만 있는 경우도 제외
	            
	            builder.and(answeredCondition);
	          } else {
	            // 답변대기: response_content가 null이거나 빈 문자열이거나 공백만 있는 경우
	            BooleanBuilder unansweredCondition = new BooleanBuilder();
	            unansweredCondition.or(inquiry.responseContent.isNull());
	            unansweredCondition.or(inquiry.responseContent.eq(""));
	            unansweredCondition.or(inquiry.responseContent.trim().eq(""));
	            
	            builder.and(unansweredCondition);
	           
	        }
	    }
	    return builder;
	}

	@Override
	public List<GatheringInquiryDto> findInquiriesReceivedByOrganizer(
	        Integer userId,
	        Date startDate,
	        Date endDate,
	        Boolean isAnswered,
	        PageRequest pageRequest
	) {
	    QGatheringInquiry inquiry = QGatheringInquiry.gatheringInquiry;
	    QGathering gathering = QGathering.gathering;
	    QUser inquiryUser = new QUser("inquiryUser");  // 문의자
	    QUser organizerUser = new QUser("organizerUser");  // 주최자
	    
	    BooleanBuilder whereCondition = new BooleanBuilder();
	    whereCondition.and(gathering.user.userId.eq(userId)); // 주최자 조건
	    
	    BooleanBuilder optionalConditions = buildCommonInquiryConditions(startDate, endDate, isAnswered);
	    
	    if (optionalConditions.hasValue()) {
	        whereCondition.and(optionalConditions);
	    }
	    
	    List<GatheringInquiry> entities = jpaQueryFactory
	        .selectFrom(inquiry)
	        .join(inquiry.gathering, gathering).fetchJoin()
	        .join(inquiry.user, inquiryUser).fetchJoin()  // 문의자 정보
	        .join(gathering.user, organizerUser).fetchJoin()  // 주최자 정보
	        .where(whereCondition)  // 통합된 조건 적용
	        .orderBy(inquiry.inquiryDate.desc())
	        .offset(pageRequest.getOffset())
	        .limit(pageRequest.getPageSize())
	        .fetch();
	    
	    return entities.stream()
	        .map(GatheringInquiry::toDto)
	        .collect(Collectors.toList());
	}

	@Override
	public Long countInquiriesSentByUser(
	        Integer userId,
	        Date startDate,
	        Date endDate,
	        Boolean isAnswered
	) throws Exception {
	    QGatheringInquiry inquiry = QGatheringInquiry.gatheringInquiry;
	    QGathering gathering = QGathering.gathering;
	    QUser organizerUser = new QUser("organizerUser");
	       
	    BooleanBuilder whereCondition = new BooleanBuilder();
	    whereCondition.and(inquiry.user.userId.eq(userId));
	    
	    BooleanBuilder optionalConditions = buildCommonInquiryConditions(startDate, endDate, isAnswered);
	    if (optionalConditions.hasValue()) {
	        whereCondition.and(optionalConditions);
	    }
	    
	    Long count = jpaQueryFactory
	        .select(inquiry.count())
	        .from(inquiry)
	        .join(inquiry.gathering, gathering)
	        .join(gathering.user, organizerUser)
	        .where(whereCondition)
	        .fetchOne();
	    
	    return count;
	}

	@Override
	public Long countInquiriesReceivedByOrganizer(
	        Integer userId,
	        Date startDate,
	        Date endDate,
	        Boolean isAnswered
	) {
	    QGatheringInquiry inquiry = QGatheringInquiry.gatheringInquiry;
	    QGathering gathering = QGathering.gathering;
	    QUser organizerUser = new QUser("organizerUser");
	    
	    BooleanBuilder whereCondition = new BooleanBuilder();
	    whereCondition.and(organizerUser.userId.eq(userId));
	    
	    BooleanBuilder optionalConditions = buildCommonInquiryConditions(startDate, endDate, isAnswered);
	    if (optionalConditions.hasValue()) {
	        whereCondition.and(optionalConditions);
	    }
	    
	    Long count = jpaQueryFactory
	        .select(inquiry.count())
	        .from(inquiry)
	        .join(inquiry.gathering, gathering)
	        .join(gathering.user, organizerUser)
	        .where(whereCondition)
	        .fetchOne();
	    return count;
	}

	@Override
	public List<GatheringInquiryDto> gatheringInquiryListBygatheringId(Integer gatheringId) throws Exception {
		QGatheringInquiry gatheringInquiry = QGatheringInquiry.gatheringInquiry;

		List<GatheringInquiry> inquiryList = jpaQueryFactory.selectFrom(gatheringInquiry)
				.leftJoin(gatheringInquiry.user).fetchJoin() // User 정보를 함께 조회
				.leftJoin(gatheringInquiry.gathering).fetchJoin() // Gathering 정보를 함께 조회
				.where(gatheringInquiry.gathering.gatheringId.eq(gatheringId))
				.orderBy(gatheringInquiry.inquiryId.desc()).fetch();

		return inquiryList.stream().map(GatheringInquiry::toDto) // 엔티티의 toDto() 메소드 활용
				.collect(Collectors.toList());
	}
	
	@Override
	@Transactional
	public void responseToGatheringInquiry(GatheringInquiryDto gatheringInquiryDto) throws Exception {
		QGatheringInquiry gatheringInquiry = QGatheringInquiry.gatheringInquiry;
	    Date today = Date.valueOf(LocalDate.now());   
		JPAUpdateClause clause = jpaQueryFactory.update(gatheringInquiry)
				.set(gatheringInquiry.responseContent, gatheringInquiryDto.getResponseContent())
				.set(gatheringInquiry.responseDate, today)
				.where(gatheringInquiry.inquiryId.eq(gatheringInquiryDto.getInquiryId()));
		clause.execute();
	}
}
