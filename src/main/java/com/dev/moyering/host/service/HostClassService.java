package com.dev.moyering.host.service;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import com.dev.moyering.admin.dto.AdminClassDto;
import com.dev.moyering.admin.dto.AdminClassSearchCond;
import com.dev.moyering.common.dto.ClassSearchRequestDto;
import com.dev.moyering.common.dto.PageResponseDto;
import com.dev.moyering.host.dto.ClassCalendarDto;
import com.dev.moyering.host.dto.HostClassDto;
import com.dev.moyering.host.dto.HostClassSearchRequestDto;
import com.dev.moyering.host.dto.HostPageResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HostClassService {
	List<HostClassDto> getRecommendHostClassesForUser(Integer userId) throws Exception;
	Integer registClass(HostClassDto hostClassDto, List<Date> dates) throws Exception;
//	Integer saveTempClass(HostClassDto hostClassDto, List<Date> dates) throws Exception;
	PageResponseDto<HostClassDto> searchClasses(ClassSearchRequestDto dto) throws Exception;
	Map<Integer,List<ClassCalendarDto>> getHostClassesWithCalendars(Integer hostId) throws Exception;
	List<HostClassDto> selectHostClassByHostId(Integer hostId) throws Exception;
	HostClassDto getClassDetail(Integer classId, Integer calendarId, Integer hostId);
	HostClassDto getClassDetailByClassID(Integer classId) throws Exception;
	HostPageResponseDto<HostClassDto> selectHostClassByHostIdWithPagination(HostClassSearchRequestDto dto) throws Exception;


	// 관리자 > 클래스 관리 (검색 조회 + 페이징)
	Page<AdminClassDto> getHostClassListForAdmin(AdminClassSearchCond cond, Pageable pageable) throws Exception;
}
