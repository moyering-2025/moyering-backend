package com.dev.moyering.host.dto;

import lombok.Data;

@Data
public class StudentSearchRequestDto {
	private Integer hostId;
	private Integer userId;
	private int page = 1;
	private int size = 10;
	private String keyword="";
}
