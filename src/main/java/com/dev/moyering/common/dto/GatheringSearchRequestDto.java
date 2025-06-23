package com.dev.moyering.common.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class GatheringSearchRequestDto {
	private String sido;
    private Integer category1; // 1차 카테고리 ID
    private Integer category2; // 2차 카테고리 ID
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer minAttendees;
    private Integer maxAttendees;
    private String title;
    private int page = 0; // 기본값 0
    private int size = 12; // 페이지당 항목 수
}
