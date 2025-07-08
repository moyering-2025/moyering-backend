package com.dev.moyering.host.dto;

import lombok.Data;

@Data
public class SettlementSearchRequestDto {
	private Integer hostId;
	private String startDate;
	private String endDate;
	private int page;
	private int size;
	
}
