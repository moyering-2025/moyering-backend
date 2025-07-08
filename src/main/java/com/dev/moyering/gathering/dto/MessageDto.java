package com.dev.moyering.gathering.dto;

import java.sql.Date;

import com.dev.moyering.gathering.entity.Gathering;
import com.dev.moyering.gathering.entity.Message;
import com.dev.moyering.user.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MessageDto {

	private Integer messageId;
	
    private Integer gatheringId;
    private String gatheringTitle;//방 제목 역할
    private String thumbnailFileName;
    private boolean gatheringState;//게더링 상태. 진행예정:1, 취소된 모임은 0
    private Integer organizerUserId;//게더링 주최자, 방장
	private Date meetingDate;

	private Integer senderId;//메시지 작성자
	private String senderNickname;
    private String senderProfile;
    private Date approvedDate;
    
    private String messageContent;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private Date writeDate;
	private Boolean messageHide;

    private Date approvalDate;
    private Date rejectionDate;
    private Boolean messageRoomState;
    
	public Message toEntity() {
		Message.MessageBuilder builder = Message.builder()
				.messageId(messageId)
				.gathering(Gathering.builder().gatheringId(gatheringId).build())
				.user(User.builder().userId(senderId).build())
				.messageContent(messageContent)
				.writeDate(writeDate)
				.messageHide(messageHide);
	    	return builder.build();
	}

	public MessageDto(Integer gatheringId, Integer senderId, String messageContent) {
		super();
		this.gatheringId = gatheringId;
		this.senderId = senderId;
		this.messageContent = messageContent;
	}
	  public MessageDto(Integer gatheringId, String title, String thumbnail, 
              Boolean canceled, Integer userId, Date meetingDate, 
              Date latestMessageDate, Date approvalDate, Date rejectionDate) {
			 this.gatheringId = gatheringId;
			 this.gatheringTitle = title;
			 this.thumbnailFileName = thumbnail;
			 this.gatheringState = canceled;
			 this.organizerUserId = userId;
			 this.meetingDate = meetingDate;
			 this.approvalDate = approvalDate;
			 this.writeDate = latestMessageDate;
			 this.rejectionDate = rejectionDate;
	}
}
