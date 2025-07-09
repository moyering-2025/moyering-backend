package com.dev.moyering.gathering.service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.moyering.gathering.dto.GatheringApplyDto;
import com.dev.moyering.gathering.dto.MessageDto;
import com.dev.moyering.gathering.repository.GatheringApplyRepository;
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
	GatheringApplyRepository gatheringApplyRepository;
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
	public List<MessageDto> getMessageListByGatheringIdAndUserId(Integer gatheringId, Integer loginId) throws Exception {
//		 Optional<Object[]> datesOptional = gatheringApplyRepository
//		            .getApprovalAndRejectionDateByGatheringIdAndUserId(gatheringId, loginId);
//		        
//		        // 2. 신청 기록이 없는 경우
//		        if (!datesOptional.isPresent()) {
//		            System.out.println("신청 기록이 없습니다: gatheringId=" + gatheringId + ", userId=" + loginId);
//		            return Collections.emptyList();
//		        }
//		        
//		        // 3. 날짜 데이터 추출
//		        Object[] dates = datesOptional.get();
//		        Date approvalDate = (Date) dates[0];  // 승인일
//		        Date rejectionDate = (Date) dates[1]; // 거절일
//		        
//		        // 4. 승인되지 않은 경우
//		        if (approvalDate == null) {
//		            System.out.println("승인되지 않은 사용자입니다: gatheringId=" + gatheringId + ", userId=" + loginId);
//		            return Collections.emptyList();
//		        }
//		List<MessageDto> messageList = messageRepository.getMessagesByGatheringId(gatheringId, loginId, approvalDate, rejectionDate);
		List<MessageDto> messageList = messageRepository.getMessagesByGatheringId(gatheringId, loginId);
//		 System.out.println("아이디 , 게더링"+loginId +", "+gatheringId);
//		 System.out.println("messageList : "+messageList);
		 return messageList;
	}
	@Override
	public List<MessageDto> getMessageRoomListUserId(Integer userId) throws Exception {
		 List<MessageDto> messageRoomList = messageRepository.getMessageRoomListUserId(userId);
			
		 return messageRoomList;
	}
}
