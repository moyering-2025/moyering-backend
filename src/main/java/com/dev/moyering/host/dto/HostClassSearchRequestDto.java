package com.dev.moyering.host.dto;

import lombok.Data;

@Data
public class HostClassSearchRequestDto {
	private Integer hostId;
	private int page = 1;
	private int size = 10;
	private String keyword="";
	private Integer category1;
	private Integer category2;
	private String status;
	private String startDate;
	private String endDate;
}
