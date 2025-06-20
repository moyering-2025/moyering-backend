package com.dev.moyering.common.dto;

import java.util.List;

import com.dev.moyering.gathering.dto.GatheringDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GatheringSearchResponseDto {
    private List<GatheringDto> content; // 게더링 목록
    private int currentPage;
    private int totalPages;
    private long totalElements;
}
