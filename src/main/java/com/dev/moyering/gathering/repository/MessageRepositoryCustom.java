package com.dev.moyering.gathering.repository;

import java.util.List;

import com.dev.moyering.gathering.dto.MessageDto;

public interface MessageRepositoryCustom {

	List<MessageDto> getMessageRoomListUserId(Integer userId) throws Exception;

	List<MessageDto> getMessageListByGatheringId(Integer gatheringId, Integer loginId) throws Exception;

}
