package com.dev.moyering.host.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.moyering.host.entity.ClassRegist;

public interface ClassRegistRepository extends JpaRepository<ClassRegist, Integer>,ClassRegistRepositoryCustom {
	List<ClassRegist> findByClassCalendarCalendarId(Integer calendarId)throws Exception;

}
