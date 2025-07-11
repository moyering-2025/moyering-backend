package com.dev.moyering.gathering.repository;

import java.util.Date;
import java.util.List;

import com.dev.moyering.gathering.dto.MessageDto;

public interface MessageRepositoryCustom {

//	List<MessageDto> getMessageRoomListUserId(Integer userId) throws Exception;

//	List<MessageDto> getMessagesByGatheringId(Integer gatheringId, Integer loginId, Date approvalDate, Date rejectionDate) throws Exception;

	List<MessageDto> getMessagesByGatheringId(Integer gatheringId, Integer loginId);

	
	
	List<MessageDto> getAvailableMessageRoomListUserId(Integer userId);

	List<MessageDto> getDisableMessageRoomListUserId(Integer userId);

	int updateMessageDisableTimeIfExists(Integer userId, Integer gatheringId, Date disableTime);

}
