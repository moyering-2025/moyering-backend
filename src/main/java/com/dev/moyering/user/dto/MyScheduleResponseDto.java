package com.dev.moyering.user.dto;

import java.util.List;

import com.dev.moyering.gathering.dto.GatheringDto;
import com.dev.moyering.host.dto.ClassCalendarDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MyScheduleResponseDto {
    private List<ClassCalendarDto> classSchedules;
    private List<GatheringDto> gatheringSchedules;
}
