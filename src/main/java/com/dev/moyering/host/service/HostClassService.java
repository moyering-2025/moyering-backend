package com.dev.moyering.host.service;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import com.dev.moyering.common.dto.ClassSearchRequestDto;
import com.dev.moyering.common.dto.ClassSearchResponseDto;
import com.dev.moyering.host.dto.ClassCalendarDto;
import com.dev.moyering.host.dto.HostClassDto;

public interface HostClassService {
	List<HostClassDto> getRecommendHostClassesForUser(Integer userId) throws Exception;
	Integer registClass(HostClassDto hostClassDto, List<Date> dates) throws Exception;
	ClassSearchResponseDto searchClasses(ClassSearchRequestDto dto) throws Exception;
	Map<Integer,List<ClassCalendarDto>> getHostClassesWithCalendars(Integer hostId) throws Exception;
	List<HostClassDto> selectHostClassByHostId(Integer hostId) throws Exception;
	HostClassDto getClassDetail(Integer classId, Integer calendarId, Integer hostId);
	HostClassDto getClassDetailByClassID(Integer classId) throws Exception;
}
