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
	
	@Override
	public List<GatheringInquiryDto> findInquiriesReceivedByOrganizer(
		    Integer organizerUserId,
		    Date startDate,
		    Date endDate,
		    Boolean isAnswered,
		    PageRequest pageRequest
		) {
		    QGatheringInquiry inquiry = QGatheringInquiry.gatheringInquiry;
		    QGathering gathering = QGathering.gathering;

		    BooleanBuilder builder = new BooleanBuilder();
		    builder.and(gathering.user.userId.eq(organizerUserId));
		    builder.and(inquiry.gathering.eq(gathering));
		    builder.and(buildInquiryConditions(startDate, endDate, isAnswered));

		    List<GatheringInquiry> entities = jpaQueryFactory
		    	    .selectFrom(inquiry)
		    	    .leftJoin(inquiry.gathering).fetchJoin()  // N+1 문제 방지
		    	    .leftJoin(inquiry.user).fetchJoin()       // N+1 문제 방지
		    	    .where(builder)
		    	    .offset(pageRequest.getOffset())
		    	    .limit(pageRequest.getPageSize())
		    	    .fetch();

		    	// 엔티티를 DTO로 변환
		    	return entities.stream()
		    	    .map(GatheringInquiry::toDto)
		    	    .collect(Collectors.toList());
		}
	
	@Override
	public long countInquiriesReceivedByOrganizer(
		    Integer organizerUserId,
		    Date startDate,
		    Date endDate,
		    Boolean isAnswered
		) {
		    QGatheringInquiry inquiry = QGatheringInquiry.gatheringInquiry;
		    QGathering gathering = QGathering.gathering;

		    BooleanBuilder builder = new BooleanBuilder();
		    builder.and(gathering.user.userId.eq(organizerUserId));
		    builder.and(inquiry.gathering.eq(gathering));
		    builder.and(buildInquiryConditions(startDate, endDate, isAnswered));

		    return jpaQueryFactory
		        .select(inquiry.count())
		        .from(inquiry)
		        .join(inquiry.gathering, gathering)
		        .where(builder)
		        .fetchOne();
		}


	private BooleanBuilder buildInquiryConditions(
		    Date startDate,
		    Date endDate,
		    Boolean isAnswered
		) {
		    QGatheringInquiry inquiry = QGatheringInquiry.gatheringInquiry;
		    BooleanBuilder builder = new BooleanBuilder();

		    if (startDate != null) {
		        builder.and(inquiry.inquiryDate.goe(startDate));
		    }
		    if (endDate != null) {
		        builder.and(inquiry.inquiryDate.loe(endDate));
		    }
		    if (isAnswered != null) {
		        if (isAnswered) {
		            builder.and(inquiry.responseContent.isNotNull().and(inquiry.responseContent.ne("")));
		        } else {
		            builder.and(inquiry.responseContent.isNull().or(inquiry.responseContent.eq("")));
		        }
		    }

		    return builder;
		}
	@Override
	public long countInquiriesSentByUser(
		    Integer userId,
		    Date startDate,
		    Date endDate,
		    Boolean isAnswered
		) {
		    QGatheringInquiry inquiry = QGatheringInquiry.gatheringInquiry;

		    BooleanBuilder builder = new BooleanBuilder();
		    builder.and(inquiry.user.userId.eq(userId));
		    builder.and(buildInquiryConditions(startDate, endDate, isAnswered));

		    return jpaQueryFactory
		        .select(inquiry.count())
		        .from(inquiry)
		        .where(builder)
		        .fetchOne();
		}
	@Override
	public List<GatheringInquiryDto> findInquiriesSentByUser(
		    Integer userId,
		    Date startDate,
		    Date endDate,
		    Boolean isAnswered,
		    PageRequest pageRequest
		) {
		    QGatheringInquiry inquiry = QGatheringInquiry.gatheringInquiry;

		    BooleanBuilder builder = new BooleanBuilder();
		    builder.and(inquiry.user.userId.eq(userId));
		    builder.and(buildInquiryConditions(startDate, endDate, isAnswered));

		    return jpaQueryFactory
		        .select(Projections.constructor(GatheringInquiryDto.class,
		                inquiry.inquiryId,           // Integer inquiryId
		                inquiry.gathering.gatheringId, // Integer gatheringId  
		                inquiry.gathering.title,     // String title
		                inquiry.gathering.user.userId, // Integer userId
		                inquiry.gathering.user.nickName, // String nickName
		                inquiry.gathering.user.profile,  // String profile
		                inquiry.inquiryContent,      // String inquiryContent
		                inquiry.inquiryDate,         // Date inquiryDate
		                inquiry.responseDate,        // Date responseDate
		                inquiry.gathering.meetingDate, // Date meetingDate (추가 필요)
		                inquiry.responseContent     // String responseContent
		        ))
		        .from(inquiry)
		        .where(builder)
		        .offset(pageRequest.getOffset())
		        .limit(pageRequest.getPageSize())
		        .fetch();
		}

}
