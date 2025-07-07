package com.dev.moyering.gathering.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.moyering.gathering.dto.GatheringApplyDto;
import com.dev.moyering.gathering.dto.MessageDto;
import com.dev.moyering.gathering.repository.GatheringInquiryRepository;
import com.dev.moyering.gathering.repository.GatheringRepository;
import com.dev.moyering.gathering.repository.MessageRepository;
@Service
public class MessageServiceImpl implements MessageService {
	@Autowired
	GatheringInquiryRepository gatheringInquiryRepository;
	@Autowired
	GatheringRepository gatheringRepository;
	@Autowired
	MessageRepository messageRepository;
	@Override
	public Boolean sendMessage(Integer gatheringId, Integer senderId, String messageContent) throws Exception {
		//MessageDto messageDto = new MessageDto(gatheringId, senderId, messageContent);
		
		return null;
	}

	@Override
	public Boolean hideMessage(Integer messageId) throws Exception {
		MessageDto messageDto;
		
		return null;
	}
	@Override
	public List<MessageDto> getMessageListByGatheringIdAndUserId(Integer gatheringId, Integer userId) throws Exception {
		 List<MessageDto> messageList = null;
		 
		 return messageList;
	}
	@Override
	public List<MessageDto> getMessageRoomListUserId(Integer userId) throws Exception {
		 List<MessageDto> messageRoomList = null;
			
		 return messageRoomList;
	}
}
