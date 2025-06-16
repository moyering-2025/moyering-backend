package com.dev.moyering.host.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.dev.moyering.host.dto.InquiryDto;
import com.dev.moyering.common.entity.User;

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
	private String className;
	@Column
	private String studentName;
	@Column
	private Date inquiryDate;
	@Column
	private String state;
	@Column
	private String iqResContent;
	@Column
	private Date ResponseDate;
	@Column
	private String content;
	@ManyToOne
	@JoinColumn(name="calendarId")
	private ClassCalendar classCalendar;
	@ManyToOne
	@JoinColumn(name="hostId")
	private Host host;
	@ManyToOne
	@JoinColumn(name="userId")
	private User user;
	
	public InquiryDto toDto() {
		InquiryDto dto = InquiryDto.builder()
				.InquiryId(InquiryId)
				.className(className)
				.inquiryDate(inquiryDate)
				.state(state)
				.iqResContent(iqResContent)
				.ResponseDate(ResponseDate)
				.content(content)
				.build();
		if(classCalendar!=null) {
			dto.setCalendarId(classCalendar.getCalendarId());
		}
		if(host!=null) {
			dto.setHostId(host.getHostId());			
		}
		if(user!=null) {
			dto.setUserId(user.getUserId());
			dto.setStudentName(user.getName());
		}
		return dto;
	}
	
	
}
