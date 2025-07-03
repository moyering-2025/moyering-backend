package com.dev.moyering.host.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.moyering.host.entity.ScheduleDetail;

public interface ScheduleDetailRepository extends JpaRepository<ScheduleDetail, Integer> {

	List<ScheduleDetail> findByHostClassClassId(Integer classId)throws Exception;
    Optional<ScheduleDetail> findFirstByHostClass_ClassIdOrderByStartTimeAsc(Integer classId);
}
