package com.dev.moyering.gathering.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dev.moyering.gathering.dto.MessageDto;
import com.dev.moyering.gathering.entity.*;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
public class MessageRepositoryImpl implements MessageRepositoryCustom{
	@Autowired
	private final JPAQueryFactory jpaQueryFactory;
	
	public List<MessageDto> getMessageRoomListUserId(Integer userId) throws Exception {
		 List<MessageDto> messageRoomList = null;
		 QGatheringApply gatheringApply = QGatheringApply.gatheringApply;
		 QGathering gathering = QGathering.gathering;
		 messageRoomList = jpaQueryFactory
		     .select(Projections.constructor(
		         MessageDto.class,
		         gathering.gatheringId,
		         gathering.title,
		         gathering.thumbnail,
		         gathering.canceled,
		         gathering.user.userId,
		         gathering.meetingDate,
		         gatheringApply.approvalDate,
		         gatheringApply.rejectionDate
		     ))
		     .from(gatheringApply)
		     .leftJoin(gatheringApply.gathering, gathering)
		     .where(
		         gatheringApply.user.userId.eq(userId)
		         .and(gatheringApply.approvalDate.isNotNull())
		         .and(gathering.canceled.eq(false))
		         
		     )
		     .fetch();
		 return messageRoomList;
	}
}
