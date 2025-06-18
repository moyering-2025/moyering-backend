package com.dev.moyering.host.dto;

import java.sql.Date;

import com.dev.moyering.host.entity.ClassCalendar;
import com.dev.moyering.host.entity.Host;
import com.dev.moyering.host.entity.Inquiry;
import com.dev.moyering.user.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InquiryDto {
	private Integer InquiryId;
	private String className;
	private String studentName;
	private Date inquiryDate;
	private String state;
	private String iqResContent;
	private Date ResponseDate;
	private String content;
	private Integer calendarId;
	private Integer hostId;
	private Integer userId; 
	
	public Inquiry toEntity() {
		Inquiry entity = Inquiry.builder()
				.InquiryId(InquiryId)
				.className(className)
				.inquiryDate(inquiryDate)
				.state(state)
				.iqResContent(iqResContent)
				.ResponseDate(ResponseDate)
				.content(content)
				.build();
		if(calendarId!=null) {
			entity.setClassCalendar(ClassCalendar.builder()
					.calendarId(calendarId).build());
		}
		if(hostId!=null) {
			entity.setHost(Host.builder()
					.hostId(hostId).build());
		}
		if(userId!=null) {
			entity.setUser(User.builder()
					.userId(userId)
					.name(studentName)
					.build());
		}
		return entity;
	}
}
