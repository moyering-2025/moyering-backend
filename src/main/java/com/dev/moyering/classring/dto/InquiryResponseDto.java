package com.dev.moyering.classring.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InquiryResponseDto {
	private Integer InquiryId;
	private String content;
	private Date inquiryDate;
	private String iqResContent;
	private Date responseDate;
	private String className;
	private String hostName;
	private Date classDate;
	private Integer state;

}
