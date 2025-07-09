package com.dev.moyering.gathering.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dev.moyering.gathering.dto.GatheringDto;
import com.dev.moyering.gathering.entity.Gathering;
import java.sql.Timestamp;
import java.util.List;
public interface GatheringRepository extends JpaRepository<Gathering, Integer>, GatheringRepositoryCustom {
	@Query("SELECT g FROM Gathering g WHERE g.applyDeadline < :currentTime AND g.canceled = false")
	List<Gathering> findExpiredGatherings(@Param("currentTime") Timestamp currentTime);
	
}
