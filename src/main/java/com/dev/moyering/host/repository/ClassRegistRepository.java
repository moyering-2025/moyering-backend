package com.dev.moyering.host.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.moyering.host.dto.ClassRegistDto;
import com.dev.moyering.host.entity.ClassRegist;

public interface ClassRegistRepository extends JpaRepository<ClassRegist, Integer>,ClassRegistRepositoryCustom {
	List<ClassRegist> findByClassCalendarCalendarId(Integer calendarId)throws Exception;
	ClassRegist findByUserUserId(Integer userInd)throws Exception;
	List<ClassRegist> findAllByUser_UserId(Integer userId) throws Exception;
	List<Integer> findAllByClassCalendar_CalendarId(Integer calendarId) throws Exception;

}
