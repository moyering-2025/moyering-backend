package com.dev.moyering.gathering.dto;

import java.util.Date;

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
	private String gatheringTitle;// 방 제목 역할
	private String thumbnailFileName;
	private boolean gatheringState;// 게더링 상태. 진행예정:1, 취소된 모임은 0
	private Integer organizerUserId;// 게더링 주최자, 방장
	private Date meetingDate;

	private Integer senderId;// 메시지 작성자
	private String senderNickname;
	private String senderProfile;

	private String messageContent;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private Date writeDate;
	private Boolean messageHide;

	private Date messageAvailableTime;
	private Date messageDisableTime;
	private Boolean messageRoomState;

	public Message toEntity() {
		Message.MessageBuilder builder = Message.builder().messageId(messageId)
				.gathering(Gathering.builder().gatheringId(gatheringId).build())
				.user(User.builder().userId(senderId).build()).messageContent(messageContent).writeDate(writeDate)
				.messageHide(messageHide)
				.messageDisableTime(messageDisableTime)
				.messageAvailableTime(messageAvailableTime);
		return builder.build();
	}
	public MessageDto(Integer gatheringId, String gatheringTitle, String thumbnailFileName, 
            Integer organizerUserId, Date meetingDate, 
            Boolean messageRoomState) {
		super();
		this.gatheringId = gatheringId;
		this.gatheringTitle = gatheringTitle;
		this.thumbnailFileName = thumbnailFileName;
		this.organizerUserId = organizerUserId;
		this.meetingDate = meetingDate;
		this.messageRoomState = messageRoomState;
		}
	public MessageDto(Integer gatheringId, String gatheringTitle, String thumbnailFileName, 
            Integer organizerUserId, Date meetingDate, Date messageAvailableTime, 
            Boolean messageRoomState) {
this.gatheringId = gatheringId;
this.gatheringTitle = gatheringTitle;
this.thumbnailFileName = thumbnailFileName;
this.organizerUserId = organizerUserId;
this.meetingDate = meetingDate;
this.messageAvailableTime = messageAvailableTime;
this.messageRoomState = messageRoomState;

// 기본값 설정
this.gatheringState = false;
this.messageHide = false;
}
	public MessageDto(Integer messageId, Integer gatheringId, Integer senderId, 
            String senderNickname, String senderProfile, String messageContent, 
            Boolean messageHide, Date writeDate) {
		this.messageId = messageId;
		this.gatheringId = gatheringId;
		this.senderId = senderId;
		this.senderNickname = senderNickname;
		this.senderProfile = senderProfile;
		this.messageContent = messageContent;
		this.messageHide = messageHide;
		this.writeDate = writeDate;
	}
}
