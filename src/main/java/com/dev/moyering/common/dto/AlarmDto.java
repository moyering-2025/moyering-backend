package com.dev.moyering.common.dto;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.dev.moyering.common.entity.Alarm;
import com.dev.moyering.user.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlarmDto {
	private Integer alarmId;
	private Integer alarmType;
	// '1: 시스템,관리자 알람 2 : 클래스링 알람, 3 : 게더링 알람, 4: 소셜링 알람',

	// 발신자 (sender)
	private String senderUserNickName;
	private Integer senderUserId;

	// 수신자 (receiver)
	private Integer receiverUserId;
	private String title;
	private String content;
	private Boolean confirm;
	private Date alarmDate;

	public Alarm toEntity() {
		return Alarm.builder()
				.alarmId(alarmId)
				.alarmType(alarmType)
				.sender(User.builder().userId(senderUserId).build())
				.receiver(User.builder().userId(receiverUserId).build())
				.title(title)
				.content(content)
				.confirm(confirm)
				.alarmDate(alarmDate)
				.build();
	}

}
