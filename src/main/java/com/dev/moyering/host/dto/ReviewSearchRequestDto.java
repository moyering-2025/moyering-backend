package com.dev.moyering.host.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class ReviewSearchRequestDto {
	private Integer hostId;
	private Integer calendarId;
	private String searchFilter;
	private String searchQuery;
	private String startDate;
	private String endDate;
	private String replyStatus;
	private Integer page;
	private Integer size;
}
