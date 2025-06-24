package com.dev.moyering.common.dto;

import java.util.List;

import com.dev.moyering.host.dto.ClassCalendarDto;
import com.dev.moyering.host.dto.HostClassDto;
import com.dev.moyering.host.dto.HostDto;
import com.dev.moyering.host.dto.ReviewDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClassRingDetailResponseDto {
    private HostClassDto hostClass;
    private List<ClassCalendarDto> calendarList;
    private List<ClassCalendarDto> currList;
    private HostDto host;
    private List<ReviewDto> reviews;
}
