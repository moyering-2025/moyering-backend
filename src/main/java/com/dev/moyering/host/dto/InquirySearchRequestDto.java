package com.dev.moyering.host.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InquirySearchRequestDto {
	private Integer hostId;
	private String searchFilter;
	private String searchQuery;
	private String startDate;
	private String endDate;
	private String replyStatus;
	private int page;
	private int size;
}
