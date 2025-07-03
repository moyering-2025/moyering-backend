package com.dev.moyering.host.repository;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import com.dev.moyering.gathering.entity.Gathering;
import com.dev.moyering.host.dto.ClassCalendarDto;
import com.dev.moyering.host.entity.ClassCalendar;
import com.dev.moyering.user.entity.User;

public interface ClassCalendarRepositoryCustom {
    Map<Integer, Date> findEarliestStartDatesByClassIds(List<Integer> classIds) throws Exception;
    List<ClassCalendar> findTop4ByDistinctHostClass() throws Exception;
	List<ClassCalendar> findRecommendClassesForUser2(User user) throws Exception;
	List<ClassCalendarDto> findAllScheduleWithDetailsByUserId(Integer userId) throws Exception;
}
