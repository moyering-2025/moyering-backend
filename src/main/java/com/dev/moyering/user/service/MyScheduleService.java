package com.dev.moyering.user.service;

import com.dev.moyering.user.dto.MyScheduleResponseDto;

public interface MyScheduleService {
	MyScheduleResponseDto getMySchedule(Integer userId) throws Exception ;

}
