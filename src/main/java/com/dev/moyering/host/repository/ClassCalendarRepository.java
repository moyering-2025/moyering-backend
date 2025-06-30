package com.dev.moyering.host.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.moyering.host.entity.ClassCalendar;

public interface ClassCalendarRepository extends JpaRepository<ClassCalendar, Integer>, ClassCalendarRepositoryCustom {
	List<ClassCalendar> findByHostClassClassIdIn(Set<Integer> classIds) throws Exception;
	Optional<ClassCalendar> findByCalendarIdAndHostClass_ClassId(Integer calendarId, Integer classId);
	List<ClassCalendar> findAllByHostClass_ClassIdAndStatus(Integer classId,String status);
	List<ClassCalendar> findByHostClassClassId(Integer classId)throws Exception;
}
