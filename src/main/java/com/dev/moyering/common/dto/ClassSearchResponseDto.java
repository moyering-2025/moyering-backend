package com.dev.moyering.common.dto;

import java.util.List;

import com.dev.moyering.host.dto.HostClassDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClassSearchResponseDto {
    private List<HostClassDto> content; // 클래스 목록
    private int currentPage;
    private int totalPages;
    private long totalElements;
}
