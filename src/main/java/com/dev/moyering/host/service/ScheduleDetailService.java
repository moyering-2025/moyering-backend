package com.dev.moyering.host.service;

import java.util.List;

import com.dev.moyering.host.dto.ScheduleDetailDto;

public interface ScheduleDetailService {
	void registScheduleDetail(String scheduleDetail,Integer classId) throws Exception;
	void updateScheduleDetail(String scheduleDetail,Integer classId)throws Exception;
	List<ScheduleDetailDto> selectScheduleDetailByClassId(Integer classId)throws Exception;

}
