package com.dev.moyering.host.service;

import java.util.List;

import com.dev.moyering.host.dto.ClassCalendarDto;
import com.dev.moyering.host.dto.HostClassDto;

public interface ClassCalendarService {
	List<HostClassDto> getHotHostClasses() throws Exception;
	List<HostClassDto> getRecommendHostClassesForUser2(Integer userId) throws Exception;
	List<ClassCalendarDto> selectCalednarByClassId(Integer classId)throws Exception;
	List<ClassCalendarDto> getClassCalendarByHostClassId(Integer classId);
}
