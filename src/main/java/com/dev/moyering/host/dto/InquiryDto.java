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
	private String content;
	private Date inquiryDate;
	private String iqResContent;
	private Date responseDate;
	private Integer calendarId;
	private Integer hostId;
	private Integer userId; 
	
	private String studentName;
	private String className;
	private String hostName;

	public Inquiry toEntity() {
		Inquiry entity = Inquiry.builder()
				.InquiryId(InquiryId)
				.inquiryDate(inquiryDate)
				.iqResContent(iqResContent)
				.responseDate(responseDate)
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
					.build());
		}
		return entity;
	}
}
