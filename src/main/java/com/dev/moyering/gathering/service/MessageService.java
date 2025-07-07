package com.dev.moyering.gathering.service;

import java.util.List;

import com.dev.moyering.gathering.dto.MessageDto;

public interface MessageService {

	Boolean sendMessage(Integer gatheringId, Integer senderId, String messageContent) throws Exception;

	Boolean hideMessage(Integer messageId) throws Exception;

	List<MessageDto> getMessageListByGatheringIdAndUserId(Integer gatheringId, Integer userId) throws Exception;

	List<MessageDto> getMessageRoomListUserId(Integer userId) throws Exception;

}
