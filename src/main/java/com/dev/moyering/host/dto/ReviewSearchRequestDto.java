package com.dev.moyering.host.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class ReviewSearchRequestDto {
	private Integer hostId;
	private String className;
	private String hostName;
	private String studentName;
	private Date startDate;
	private Date endDate;
	private Integer page = 0;
	private Integer size = 10;

}
