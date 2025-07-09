package com.dev.moyering.gathering.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import com.dev.moyering.gathering.dto.MessageDto;
import com.dev.moyering.user.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer messageId;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="gatheringId")
	private Gathering gathering;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="userId")
	private User user;
	
	@Column(columnDefinition = "DATETIME", nullable = false)
	@CreationTimestamp
	private Date writeDate;
	
	@Column(nullable = false)
    private String messageContent;

    @Column(columnDefinition = "TINYINT")
	@ColumnDefault("0")
    private Boolean messageHide;
    
	@Column(columnDefinition = "DATETIME")
	@CreationTimestamp
	private Date messageDisableTime;
	@Column(columnDefinition = "DATETIME")
	@CreationTimestamp
	private Date messageAvailableTime;
	
    public MessageDto toDto() {
    	MessageDto.MessageDtoBuilder builder = MessageDto.builder()
				.messageId(messageId)
	   			.gatheringId(gathering.getGatheringId())
				.gatheringTitle(gathering.getTitle())
				.thumbnailFileName(gathering.getThumbnail())
				.meetingDate(gathering.getMeetingDate())
				.organizerUserId(gathering.getUser().getUserId())
				.gatheringState(gathering.getCanceled())
				.senderId(user.getUserId())
				.senderNickname(user.getNickName())
				.senderProfile(user.getProfile())
				.messageContent(messageContent)
				.writeDate(writeDate)
				.messageHide(messageHide)
				.messageDisableTime(messageDisableTime)
				.messageAvailableTime(messageAvailableTime);
	    	return builder.build();
    }
    
}
