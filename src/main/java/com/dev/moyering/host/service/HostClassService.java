package com.dev.moyering.host.service;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import com.dev.moyering.admin.dto.AdminClassDetailDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dev.moyering.admin.dto.AdminClassDto;
import com.dev.moyering.admin.dto.AdminClassSearchCond;
import com.dev.moyering.common.dto.ClassSearchRequestDto;
import com.dev.moyering.common.dto.PageResponseDto;
import com.dev.moyering.host.dto.CalendarUserDto;
import com.dev.moyering.host.dto.ClassCalendarDto;
import com.dev.moyering.host.dto.HostClassDto;
import com.dev.moyering.host.dto.HostClassSearchRequestDto;
import com.dev.moyering.host.dto.HostPageResponseDto;
import com.dev.moyering.host.dto.StudentSearchRequestDto;
import com.dev.moyering.user.dto.UserDto;

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
	Integer updateClass(HostClassDto hostClassDto) throws Exception;

	// 관리자 > 클래스 관리 (검색 조회 + 페이징)
	Page<AdminClassDto> getHostClassListForAdmin(AdminClassSearchCond cond, Pageable pageable) throws Exception;

	// 관리자 > 클래스 승인
	void approveClass(Integer classId) throws Exception;

	// 관리자 > 틀래스 거절
	void rejectClass(Integer classId) throws Exception;

	 // 관리자 > 클래스 관리 > 클래스 상세
	AdminClassDetailDto getClassDetailForAdmin(Integer classId) throws Exception;

	List<UserDto> selectClassStudentList(Integer calendarId) throws Exception;
	List<UserDto> selectStudentList(Integer hostId)throws Exception;
	
	Page<UserDto> searchStudents(StudentSearchRequestDto dto) throws Exception;
	
	List<CalendarUserDto> searchStudentClass(Integer hostId,Integer userId)throws Exception;
	//클래스 상세 추천 강의
	List<HostClassDto> getRecommendClassesInDetail(Integer subCategoryId, Integer classId)throws Exception;
	
	Map<String,Object> hostRateCount(Integer hostId)throws Exception; 
	
}
