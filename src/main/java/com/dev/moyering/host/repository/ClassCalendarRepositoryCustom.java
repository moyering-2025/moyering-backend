package com.dev.moyering.host.repository;

import java.sql.Date;
import java.util.List;
import java.util.Map;

public interface ClassCalendarRepositoryCustom {
    Map<Integer, Date> findEarliestStartDatesByClassIds(List<Integer> classIds) throws Exception;
}
