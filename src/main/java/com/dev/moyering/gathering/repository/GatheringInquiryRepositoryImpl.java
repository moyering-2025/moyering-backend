package com.dev.moyering.gathering.repository;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import com.dev.moyering.gathering.dto.GatheringInquiryDto;
import com.dev.moyering.gathering.entity.GatheringInquiry;
import com.dev.moyering.gathering.entity.QGathering;
import com.dev.moyering.gathering.entity.QGatheringInquiry;
import com.dev.moyering.user.entity.QUser;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GatheringInquiryRepositoryImpl implements GatheringInquiryRepositoryCustom {

	@Autowired
	private final JPAQueryFactory jpaQueryFactory;
	@Autowired
	private ModelMapper modelMapper;

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
	public List<GatheringInquiryDto> findGatheringInquiriesByUserAndPeriod(PageRequest pageRequest, Map<String, Object> params) {
	    
	    QGatheringInquiry gatheringInquiry = QGatheringInquiry.gatheringInquiry;
	    QUser inquiryUser = new QUser("inquiryUser");  // 문의 작성자
	    QGathering gathering = QGathering.gathering;
			// 파라미터 추출
		Integer organizerUserId = (Integer) params.get("gatheringOrganizer");
		Date startDate = (Date) params.get("startDate");
		Date endDate = (Date) params.get("endDate");
		Boolean isAnswered = (Boolean) params.get("isAnswered");
	    JPAQuery<Tuple> query = jpaQueryFactory.select(gatheringInquiry, gathering.title, gathering.meetingDate, inquiryUser.nickName, inquiryUser.profile)
	    	    .from(gatheringInquiry)
	    	    .leftJoin(gathering).on(gatheringInquiry.gathering.gatheringId.eq(gathering.gatheringId))
	    	    .leftJoin(inquiryUser).on(gatheringInquiry.user.userId.eq(inquiryUser.userId))
	    	    .where(buildWhereConditions(gathering, gatheringInquiry, organizerUserId, startDate, endDate, isAnswered))
	    	    .orderBy(gatheringInquiry.inquiryDate.desc())
	    	    .offset(pageRequest.getOffset())
	    	    .limit(pageRequest.getPageSize());
	        
	    return query.fetch().stream()
	        .map(tuple -> tuple.get(0, GatheringInquiry.class).toDto())
	        .collect(Collectors.toList());
	}

	// WHERE 조건을 동적으로 생성하는 메서드
	private BooleanExpression buildWhereConditions(QGathering gathering, QGatheringInquiry gatheringInquiry,
	        Integer organizerUserId, Date startDate, Date endDate, Boolean isAnswered) {
	    // 기본 조건: 주최자 ID
	    BooleanExpression condition = gathering.user.userId.eq(organizerUserId);
	    boolean isStartDateEmpty = (startDate== null || startDate.toString().trim().isEmpty());
	    boolean isEndDateEmpty = (endDate== null || endDate.toString().trim().isEmpty());
	    // 날짜 범위 조건 추가
	    if (!isStartDateEmpty || !isEndDateEmpty) {
	        BooleanExpression dateCondition = null;
	        if (!isStartDateEmpty) {
	            dateCondition = gatheringInquiry.inquiryDate.goe(startDate);
	        }
	        if (!isEndDateEmpty) {
	            BooleanExpression endCondition = gatheringInquiry.inquiryDate.loe(endDate);
	            dateCondition = (dateCondition != null) ? dateCondition.and(endCondition) : endCondition;
	        }
	        condition = condition.and(dateCondition);
	    }
	    // 답변 완료 여부 조건 추가
	    if (isAnswered != null) {
	        BooleanExpression answerCondition;
	        if (isAnswered) {
	            answerCondition = gatheringInquiry.responseContent.isNotNull()
	                    .and(gatheringInquiry.responseContent.ne(""));
	        } else {
	            answerCondition = gatheringInquiry.responseContent.isNull()
	                    .or(gatheringInquiry.responseContent.eq(""));
	        }
	        condition = condition.and(answerCondition);
	    }
	    return condition;
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
	public Long selectInquiryCount(Map<String, Object> params) {
		QGatheringInquiry gatheringInquiry = QGatheringInquiry.gatheringInquiry;
		QGathering gathering = QGathering.gathering;

		// 파라미터 추출
		Integer organizerUserId = (Integer) params.get("gatheringOrganizer");
		Date startDate = (Date) params.get("startDate");
		Date endDate = (Date) params.get("endDate");
		Boolean isAnswered = (Boolean) params.get("isAnswered");

		// 기존 조건 생성 메서드들을 재사용하여 count 쿼리 생성
		return jpaQueryFactory.select(gatheringInquiry.count()).from(gatheringInquiry).leftJoin(gathering)
				.on(gatheringInquiry.gathering.gatheringId.eq(gathering.gatheringId))
				.where(buildWhereConditions(gathering, gatheringInquiry, organizerUserId, startDate, endDate,
						isAnswered))
				.fetchOne();
	}
}
