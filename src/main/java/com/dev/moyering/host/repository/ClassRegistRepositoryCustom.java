package com.dev.moyering.host.repository;

import java.util.List;

import com.dev.moyering.host.dto.CalendarUserDto;
import com.dev.moyering.host.entity.ClassRegist;

public interface ClassRegistRepositoryCustom {
	List<ClassRegist> findByCalendar_HostClass_Host_HostId(Integer hostId);
	
	List<CalendarUserDto> findByStudentClass(Integer hostId,Integer userId);

}
