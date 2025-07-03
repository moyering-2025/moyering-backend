package com.dev.moyering.user.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dev.moyering.gathering.dto.GatheringDto;
import com.dev.moyering.gathering.service.GatheringService;
import com.dev.moyering.host.dto.ClassCalendarDto;
import com.dev.moyering.host.service.ClassCalendarService;
import com.dev.moyering.user.dto.MyScheduleResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyScheduleServiceImpl implements MyScheduleService {
	private final ClassCalendarService classCalendarService;
	private final GatheringService gatheringService;
	
	@Override
	public MyScheduleResponseDto getMySchedule(Integer userId) throws Exception {
		List<ClassCalendarDto> classList = classCalendarService.getMyClassSchedule(userId);
		List<GatheringDto> gatheringList = gatheringService.getMyGatheringSchedule(userId);
		return MyScheduleResponseDto.builder()
				.classSchedules(classList)
				.gatheringSchedules(gatheringList)
				.build();
	}

}
