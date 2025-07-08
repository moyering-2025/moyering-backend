package com.dev.moyering.gathering.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dev.moyering.gathering.dto.MessageDto;
import com.dev.moyering.gathering.entity.*;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
public class MessageRepositoryImpl implements MessageRepositoryCustom{
	@Autowired
	private final JPAQueryFactory jpaQueryFactory;
	private QGatheringApply gatheringApply = QGatheringApply.gatheringApply;
	private	QGathering gathering = QGathering.gathering;
	private QMessage message = QMessage.message;
	@Override
	public List<MessageDto> getMessageRoomListUserId(Integer userId) throws Exception {
	    return jpaQueryFactory
	        .selectDistinct(Projections.constructor(
	            MessageDto.class,
	            gathering.gatheringId,
	            gathering.title,
	            gathering.thumbnail,
	            gathering.canceled,
	            gathering.user.userId,
	            gathering.meetingDate,
	            message.writeDate.max().as("latestMessageDate"),
	            gatheringApply.approvalDate,
	            gatheringApply.rejectionDate	            
	        ))
	        .from(gathering)
	        .leftJoin(gatheringApply).on(gatheringApply.gathering.gatheringId.eq(gathering.gatheringId)
                .and(gatheringApply.user.userId.eq(userId)))
            .leftJoin(message).on(message.gathering.gatheringId.eq(gathering.gatheringId))
            .where(
	            ((gatheringApply.user.userId.eq(userId)
	   	             .and(gatheringApply.aspiration.isNotNull())
		             .and(gathering.canceled.eq(false)))
		            .or(gathering.user.userId.eq(userId)))
	        )
	        .groupBy(
	            gathering.gatheringId,
	            gathering.title,
	            gathering.thumbnail,
	            gathering.meetingDate,
	            gathering.canceled,
	            gathering.user.userId,
	            gatheringApply.approvalDate
	        )
	        .orderBy(
	            gathering.canceled.asc(),           // 취소되지 않은 게더링 우선
	            message.writeDate.max().desc(),     // 최신 메시지 날짜 순
	            gathering.meetingDate.asc()         // 모임일 가까운 순
	        )
	        .fetch();
	}
	@Override
	public List<MessageDto> getMessageListByGatheringId(Integer gatheringId, Integer loginId) throws Exception {
	    return jpaQueryFactory
	        .selectDistinct(Projections.constructor(
	            MessageDto.class,
	            message.gathering.gatheringId,
	            message.gathering.title,
	            message.messageId,
	            message.user.userId,
	            message.user.nickName,
	            message.user.profile,
	            new CaseBuilder()
	                .when(message.messageHide.isTrue())
	                .then("주최자가 가린 메시지 입니다")
	                .otherwise(message.messageContent)
	                .as("messageContent"),
	            message.messageHide,
	            message.writeDate,
	            message.hasLeft
	        ))
	        .from(message)
	        .innerJoin(gatheringApply)
	            .on(message.gathering.gatheringId.eq(gatheringApply.gathering.gatheringId))
	        .where(
	            gatheringApply.user.userId.eq(loginId)
	                .and(message.gathering.gatheringId.eq(gatheringId))
	                .and(gatheringApply.approvalDate.isNotNull())
	                .and(message.writeDate.goe(gatheringApply.approvalDate))
	                .and(gatheringApply.rejectionDate.isNull()
	                    .or(message.writeDate.lt(gatheringApply.rejectionDate)))
	        )
	        .orderBy(message.writeDate.asc())
	        .fetch();
	}
}
