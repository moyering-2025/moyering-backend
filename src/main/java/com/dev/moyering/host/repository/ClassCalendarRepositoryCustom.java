package com.dev.moyering.host.repository;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import com.dev.moyering.host.entity.ClassCalendar;

public interface ClassCalendarRepositoryCustom {
    Map<Integer, Date> findEarliestStartDatesByClassIds(List<Integer> classIds) throws Exception;
    List<ClassCalendar> findTop4ByDistinctHostClass() throws Exception;
}
