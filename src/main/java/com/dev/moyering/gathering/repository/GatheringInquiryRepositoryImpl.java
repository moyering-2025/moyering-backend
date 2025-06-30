package com.dev.moyering.gathering.repository;

import java.sql.Date;
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
		JPAUpdateClause clause = jpaQueryFactory.update(gatheringInquiry)
				.set(gatheringInquiry.responseContent, gatheringInquiryDto.getInquiryContent())
				.set(gatheringInquiry.responseDate, gatheringInquiryDto.getResponseDate())
				.where(gatheringInquiry.inquiryId.eq(gatheringInquiryDto.getInquiryId()));
		clause.execute();
	}
	/**
	 * 문의 조회용 공통 조건 생성 (필수 조건 제외)
	 */
	private BooleanBuilder buildCommonInquiryConditions(
	        Date startDate,
	        Date endDate,
	        Boolean isAnswered
	) {
	    QGatheringInquiry inquiry = QGatheringInquiry.gatheringInquiry;
	    
	    BooleanBuilder builder = new BooleanBuilder();
	    
	    // 선택 조건: 시작일
	    if (startDate != null) {
	        builder.and(inquiry.inquiryDate.goe(startDate));
	    }
	    
	    // 선택 조건: 종료일
	    if (endDate != null) {
	        builder.and(inquiry.inquiryDate.loe(endDate));
	    }
	    
	    // 선택 조건: 답변 상태
	    if (isAnswered != null) {
	        if (isAnswered) {
	            // 답변완료: response_content가 null이 아니고 빈 문자열이 아님
	            builder.and(inquiry.responseContent.isNotNull()
	                       .and(inquiry.responseContent.ne("")));
	        } else {
	            // 미답변: response_content가 null이거나 빈 문자열
	            builder.and(inquiry.responseContent.isNull()
	                       .or(inquiry.responseContent.eq("")));
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
	    
	    // 공통 조건 생성 (선택 조건들만)
	    BooleanBuilder optionalConditions = buildCommonInquiryConditions(startDate, endDate, isAnswered);
	    
	    List<GatheringInquiry> entities = jpaQueryFactory
	        .selectFrom(inquiry)
	        .join(inquiry.gathering, gathering).fetchJoin()
	        .join(inquiry.user, inquiryUser).fetchJoin()  // 문의자 정보
	        .join(gathering.user, organizerUser).fetchJoin()  // 주최자 정보
	        .where(organizerUser.userId.eq(userId))  // 필수 조건: 주최자 ID
	        .where(optionalConditions)  // 선택 조건들
	        .orderBy(inquiry.inquiryDate.desc())
	        .offset(pageRequest.getOffset())
	        .limit(pageRequest.getPageSize())
	        .fetch();
	    
	    return entities.stream()
	        .map(GatheringInquiry::toDto)
	        .collect(Collectors.toList());
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
	    QUser organizerUser = new QUser("organizerUser");  // 주최자
	    
	    // 공통 조건 생성 (선택 조건들만)
	    BooleanBuilder optionalConditions = buildCommonInquiryConditions(startDate, endDate, isAnswered);
	    
	    return jpaQueryFactory
	        .select(inquiry.count())
	        .from(inquiry)
	        .join(inquiry.gathering, gathering)
	        .join(gathering.user, organizerUser)
	        .where(organizerUser.userId.eq(userId))  // 필수 조건: 주최자 ID
	        .where(optionalConditions)  // 선택 조건들
	        .fetchOne();
	}

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
	    
	    // 공통 조건 생성 (선택 조건들만)
	    BooleanBuilder optionalConditions = buildCommonInquiryConditions(startDate, endDate, isAnswered);
	    
	    List<GatheringInquiry> entities = jpaQueryFactory
	        .selectFrom(inquiry)
	        .join(inquiry.gathering, gathering).fetchJoin()
	        .join(inquiry.user, inquiryUser).fetchJoin()  // 문의자 정보
	        .join(gathering.user, organizerUser).fetchJoin()  // 주최자 정보
	        .where(inquiry.user.userId.eq(userId))  // 필수 조건: 문의자 ID
	        .where(optionalConditions)  // 선택 조건들
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
	    QUser organizerUser = new QUser("organizerUser");  // 주최자
	    
	    // 공통 조건 생성 (선택 조건들만)
	    BooleanBuilder optionalConditions = buildCommonInquiryConditions(startDate, endDate, isAnswered);
	    
	    return jpaQueryFactory
	        .select(inquiry.count())
	        .from(inquiry)
	        .join(inquiry.gathering, gathering)
	        .join(gathering.user, organizerUser)
	        .where(inquiry.user.userId.eq(userId))  // 필수 조건: 문의자 ID
	        .where(optionalConditions)  // 선택 조건들
	        .fetchOne();
	}
}
