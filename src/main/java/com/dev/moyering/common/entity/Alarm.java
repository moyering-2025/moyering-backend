package com.dev.moyering.common.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.dev.moyering.common.dto.AlarmDto;
import com.dev.moyering.user.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
public class Alarm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer alarmId;
    @Column(nullable = false)
    private Integer alarmType;
    //'1: 시스템,관리자 알람 2 : 클래스링 알람, 3 : 게더링 알람, 4: 소셜링 알람',

    // 발신자 (sender)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "s_user_id", nullable = false)
    private User sender;

	@Column
	private String senderNickName;
	
    // 수신자 (receiver)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "r_user_id", nullable = false)
    private User receiver;

	@Column
	private String title;

	@Column
	private String content;
	
	@Column
	private Boolean confirm;
	
    @Column(nullable = false)
    private Date alarmDate;
    public AlarmDto toDto() {
    	return AlarmDto.builder()
				.alarmId(alarmId)
				.alarmType(alarmType)
				.senderUserId(sender.getUserId())
				.senderUserNickName(sender.getNickName())
				.receiverUserId(receiver.getUserId())
				.title(title)
				.content(content)
				.confirm(confirm)
				.alarmDate(alarmDate)
				.build();
    }
}
