package com.dev.moyering.host.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.moyering.host.entity.ClassCalendar;

public interface ClassCalendarRepository extends JpaRepository<ClassCalendar, Integer>, ClassCalendarRepositoryCustom {
}
