package com.dev.moyering.dto.host;

import java.sql.Date;

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
	private Integer studentId;
	private String className;
	private String studentName;
	private Date inquiryDate;
	private String state;
	private String iqResContent;
	private Date ResponseDate;
	private Integer calendarId;
	private Integer hostId;
	private String content;
}
