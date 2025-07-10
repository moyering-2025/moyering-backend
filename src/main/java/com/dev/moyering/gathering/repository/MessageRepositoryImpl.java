package com.dev.moyering.gathering.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dev.moyering.gathering.dto.MessageDto;
import com.dev.moyering.gathering.entity.QGathering;
import com.dev.moyering.gathering.entity.QGatheringApply;
import com.dev.moyering.gathering.entity.QMessage;
import com.dev.moyering.user.entity.QUser;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MessageRepositoryImpl implements MessageRepositoryCustom {
	@Autowired
	private final JPAQueryFactory jpaQueryFactory;
	private QGatheringApply gatheringApply = QGatheringApply.gatheringApply;
	private QGathering gathering = QGathering.gathering;
	private QMessage message = QMessage.message;
	private QUser user = QUser.user;
//	@Override
//	public List<MessageDto> getMessagesByGatheringId(Integer gatheringId, Integer loginId,Date approvalDate,Date rejectionDate ) throws Exception {
//		 
//	    BooleanBuilder dateCondition = new BooleanBuilder();
//	    
//	    // 날짜 범위 조건 구성
//	    if (approvalDate != null && rejectionDate != null) {
//	        // 승인일 이후이면서 거절일 이전
//	        dateCondition.and(message.writeDate.between(approvalDate, rejectionDate));
//	    } else if (approvalDate != null) {
//	        // 승인일 이후만
//	        dateCondition.and(message.writeDate.goe(approvalDate));
//	    } else if (rejectionDate != null) {
//	        // 거절일 이전만 (일반적이지 않은 케이스)
//	        dateCondition.and(message.writeDate.loe(rejectionDate));
//	    }
//	    // approvalDate와 rejectionDate가 모두 null인 경우는 조건 없음
//	    
//	    List<MessageDto> messages = jpaQueryFactory
//	        .select(Projections.constructor(MessageDto.class,
//	            message.gathering.gatheringId,
//	            message.messageId,
//	            message.user.userId,
//	            message.user.nickName,
//	            message.user.profile,
//	            message.messageHide,
//	            message.writeDate,
//	            new CaseBuilder()
//	                .when(message.messageHide.isTrue())
//	                .then("주최자가 가린 메시지 입니다.")
//	                .otherwise(message.messageContent)))
//	        .from(message)
//	        .join(message.gathering, gathering)
//	        .where(
//	            gathering.gatheringId.eq(gatheringId),
//	            dateCondition
//	        )
//	        .orderBy(message.writeDate.asc(), message.messageId.asc())
//	        .fetch();
//	    
//	    return messages;
//	}
//	@Override
//	public List<MessageDto> getMessageRoomListUserId(Integer userId) throws Exception {
//		java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
//		return jpaQueryFactory
//				.selectDistinct(Projections.constructor(MessageDto.class, 
//						gathering.gatheringId, 
//						gathering.title,
//						gathering.thumbnail, 
//						gathering.canceled, 
//						gathering.user.userId, 
//						gathering.meetingDate,
//						new CaseBuilder()
//								.when(gathering.meetingDate.before(today).or(gathering.canceled.eq(true))
//										.or(gatheringApply.isNull()))
//								.then(false).otherwise(true).as("messageRoomState")))
//				.from(gathering).leftJoin(gatheringApply)
//				.on(gatheringApply.gathering.gatheringId.eq(gathering.gatheringId)
//						.and(gatheringApply.user.userId.eq(userId)))
//				.leftJoin(message).on(message.gathering.gatheringId.eq(gathering.gatheringId))
//				.where(((gatheringApply.user.userId.eq(userId).and(gatheringApply.aspiration.isNotNull())
//						.and(gathering.canceled.eq(false))).or(gathering.user.userId.eq(userId))))
//				.groupBy(gathering.gatheringId, gathering.title, gathering.thumbnail, gathering.meetingDate,
//						gathering.startTime, gathering.canceled, gathering.user.userId, 
//						message.messageDisableTime, message.messageAvailableTime)
//				.orderBy(
//						// 1. 취소되지 않은 게더링 최우선
//						gathering.canceled.asc(),
//						// 2. 모임일이 미래인지 과거인지에 따라 다른 정렬
//						new CaseBuilder().when(gathering.meetingDate.goe(today)).then(0) // 미래 모임
//								.otherwise(1) // 과거 모임
//								.asc(),
//						// 3. 미래 모임인 경우: 가까운 날짜순, 과거 모임인 경우: 최신 날짜순
//						new CaseBuilder().when(gathering.meetingDate.goe(today)).then(gathering.meetingDate.dayOfYear()) // 미래는
//																															// 오름차순을
//																															// 위해
//																															// dayOfYear
//																															// 사용
//								.otherwise(gathering.meetingDate.dayOfYear().multiply(-1)) // 과거는 내림차순을 위해 음수 변환
//								.asc(),
//						// 4. 시간 정렬 (미래는 오름차순, 과거는 내림차순)
//						new CaseBuilder().when(gathering.meetingDate.goe(today))
//								.then(gathering.startTime.hour().multiply(60).add(gathering.startTime.minute()))
//								.otherwise(gathering.startTime.hour().multiply(60).add(gathering.startTime.minute())
//										.multiply(-1))
//								.asc(),
//						// 5. 최신 메시지 날짜순 (보조 정렬)
//						message.writeDate.max().desc().nullsLast())
//				.fetch();
//	}
	@Override
	public List<MessageDto> getAvailableMessageRoomListUserId(Integer userId) {
	    QGathering gathering = QGathering.gathering;
	    QGatheringApply gatheringApply = QGatheringApply.gatheringApply;
	    
	    java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
	    LocalDateTime now = LocalDateTime.now();
	    
	    return jpaQueryFactory
	            .selectDistinct(Projections.constructor(MessageDto.class,
	                    gathering.gatheringId,
	                    gathering.title,
	                    gathering.thumbnail,
	                    gathering.user.userId,
	                    gathering.meetingDate,
	                    new CaseBuilder()
	                            .when(gathering.meetingDate.before(today)
	                                    .or(gathering.canceled.eq(true))
	                                    .or(gathering.gatheringId.notIn(
	                                            JPAExpressions
	                                                    .select(gatheringApply.gathering.gatheringId)
	                                                    .from(gatheringApply)
	                                                    .where(gatheringApply.user.userId.eq(userId)
	                                                            .and(gatheringApply.isApproved.eq(true))))
	                                            .and(gathering.user.userId.ne(userId))))
	                            .then(false)
	                            .otherwise(true)
	                            .as("messageRoomState")
	            ))
	            .from(gathering)
	            .where(
	                    gathering.canceled.eq(false)
	                    .and(gathering.gatheringId.in(
	                            JPAExpressions
	                                    .select(gatheringApply.gathering.gatheringId)
	                                    .from(gatheringApply)
	                                    .where(gatheringApply.user.userId.eq(userId)
	                                            .and(gatheringApply.isApproved.eq(true))))
	                            .or(gathering.user.userId.eq(userId)))
	                    .and(gathering.meetingDate.after(today)
	                            .or(gathering.meetingDate.eq(today)
	                                    .and(gathering.startTime.after(now.toLocalTime()))))
	            )
	            .orderBy(gathering.meetingDate.asc(), gathering.startTime.asc())
	            .fetch();
	}
	@Override
	public List<MessageDto> getDisableMessageRoomListUserId(Integer userId) {
	    return jpaQueryFactory
	            .selectDistinct(Projections.constructor(MessageDto.class,
	                    gathering.gatheringId,
	                    gathering.title,
	                    gathering.thumbnail,
	                    gathering.user.userId,
	                    gathering.meetingDate,
	                    Expressions.constant(false)
	            ))
	            .from(gathering)
	            .where(
	                    gathering.gatheringId.in(
	                            JPAExpressions
	                                    .select(message.gathering.gatheringId)
	                                    .from(message)
	                                    .where(message.user.userId.eq(userId)
	                                            .and(message.messageDisableTime.isNotNull())))
	            )
	            .orderBy(gathering.meetingDate.desc(), gathering.startTime.desc())
	            .fetch();
	}

	@Override
	public List<MessageDto> getMessagesByGatheringId(Integer gatheringId, Integer loginId) {
		QMessage m = message;
	    QMessage userTime = new QMessage("userTime");
	    QUser u = new QUser("u");
	    return jpaQueryFactory
	        .selectDistinct(Projections.constructor(
	            MessageDto.class,
	            m.gathering.gatheringId,
	            m.messageId,
	            m.user.userId,
	            u.nickName,
	            u.profile,
	            new CaseBuilder()
	                .when(m.messageHide.eq(true))
	                .then("모임장이 가린 메시지 입니다")
	                .otherwise(m.messageContent),
	            m.messageHide,
	            m.writeDate
	        ))
	        .from(m)
	        .join(u).on(m.user.userId.eq(u.userId))
	        .join(userTime).on(
	            userTime.gathering.gatheringId.eq(gatheringId)
	            .and(userTime.user.userId.eq(loginId))
	        )
	        .where(
	            m.gathering.gatheringId.eq(gatheringId)
	            .and(
	                userTime.messageAvailableTime.isNull()
	                    .and(userTime.messageDisableTime.isNull())
	                .or(
	                    userTime.messageAvailableTime.isNull()
	                    .and(m.writeDate.loe(userTime.messageDisableTime))
	                )
	                .or(
	                    userTime.messageDisableTime.isNull()
	                    .and(m.writeDate.goe(userTime.messageAvailableTime))
	                )
	                .or(
	                    userTime.messageAvailableTime.isNotNull()
	                    .and(userTime.messageDisableTime.isNotNull())
	                    .and(m.writeDate.between(
	                        userTime.messageAvailableTime,
	                        userTime.messageDisableTime
	                    ))
	                )
	            )
	        )
	        .orderBy(m.writeDate.asc(), m.messageId.asc())
	        .fetch();
	}
	@Override
	public int updateMessageDisableTimeIfExists(Integer userId, Integer gatheringId, Date disableTime) {
	    // 해당 사용자가 해당 게더링에서 작성한 메시지가 있는지 확인 후 업데이트
	    long updatedCount = jpaQueryFactory
	            .update(message)
	            .set(message.messageDisableTime, disableTime)
	            .where(
	                    message.user.userId.eq(userId)  // 거절된 사용자가 작성한 메시지
	                    .and(message.gathering.gatheringId.eq(gatheringId))  // 해당 게더링의 메시지
	                    .and(message.messageDisableTime.isNull())  // 아직 비활성화되지 않은 메시지만
	            )
	            .execute();
	    
	    return (int) updatedCount;
	}
}
