package com.dev.moyering.host.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.moyering.host.entity.ClassCalendar;
import com.dev.moyering.host.entity.HostClass;

public interface HostClassRepository extends JpaRepository<HostClass, Integer>, HostClassRepositoryCustom {
	List<HostClass> findByHostHostId(Integer hostId) throws Exception;
	HostClass findByClassId(Integer classId) throws Exception;
	Optional<HostClass> findByClassIdAndHost_HostId(Integer classId, Integer hostId);



}
