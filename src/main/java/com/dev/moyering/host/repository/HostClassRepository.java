package com.dev.moyering.host.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.moyering.host.dto.HostClassDto;
import com.dev.moyering.host.entity.HostClass;

public interface HostClassRepository extends JpaRepository<HostClass, Integer>, HostClassRepositoryCustom {
	List<HostClass> findByHostHostId(Integer hostId) throws Exception;

}
