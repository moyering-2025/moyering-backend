package com.dev.moyering.host.service;

import java.util.List;

import com.dev.moyering.host.dto.ClassCalendarDto;
import com.dev.moyering.host.dto.HostClassDto;

public interface ClassCalendarService {
	List<HostClassDto> getHotHostClasses() throws Exception;
	List<HostClassDto> getRecommendHostClassesForUser2(Integer userId) throws Exception;
	List<ClassCalendarDto> selectCalednarByClassId(Integer classId)throws Exception;
	List<ClassCalendarDto> getClassCalendarByHostClassId(Integer classId) throws Exception;
	List<ClassCalendarDto> getMyClassSchedule(Integer userId) throws Exception;
	//스케줄러로 모집마감, 폐강 처리
	void checkHostClassStatus() throws Exception;
	void changeStatusToFinished() throws Exception;
}
