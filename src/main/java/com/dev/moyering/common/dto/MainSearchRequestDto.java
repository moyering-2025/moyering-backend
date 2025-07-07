package com.dev.moyering.common.dto;

import lombok.Data;

@Data
public class MainSearchRequestDto {

	private String searchQuery;
	private int page;
	private int size;
}
