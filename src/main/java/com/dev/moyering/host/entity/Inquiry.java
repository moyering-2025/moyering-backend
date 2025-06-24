package com.dev.moyering.host.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.dev.moyering.host.dto.InquiryDto;
import com.dev.moyering.user.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Inquiry {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer InquiryId;
	@Column
	private String content;
	@Column
	private Date inquiryDate;
	@Column
	private String iqResContent;
	@Column
	private Date responseDate;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="calendarId")
	private ClassCalendar classCalendar;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="hostId")
	private Host host;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="userId")
	private User user;
	
	public InquiryDto toDto() {
		InquiryDto dto = InquiryDto.builder()
				.InquiryId(InquiryId)
				.inquiryDate(inquiryDate)
				.iqResContent(iqResContent)
				.content(content)
				.build();
		if(classCalendar!=null) {
			dto.setCalendarId(classCalendar.getCalendarId());
			dto.setClassName(classCalendar.getClass().getName());
		}
		if(host!=null) {
			dto.setHostId(host.getHostId());	
			dto.setHostName(host.getName());
		}
		if(user!=null) {
			dto.setUserId(user.getUserId());
			dto.setStudentName(user.getName());
		}
		return dto;
	}
	
	
}
