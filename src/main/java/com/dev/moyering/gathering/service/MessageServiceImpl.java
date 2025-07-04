package com.dev.moyering.gathering.service;

import org.springframework.stereotype.Service;

import com.dev.moyering.gathering.dto.MessageDto;
@Service
public class MessageServiceImpl implements MessageService {

	@Override
	public Boolean sendMessage(Integer gatheringId, Integer senderId, String messageContent) throws Exception {
		MessageDto messageDto = new MessageDto(gatheringId, senderId, messageContent);
		return null;
	}
}
