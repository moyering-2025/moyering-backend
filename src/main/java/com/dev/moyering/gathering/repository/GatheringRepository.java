package com.dev.moyering.gathering.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.moyering.gathering.dto.GatheringDto;
import com.dev.moyering.gathering.entity.Gathering;

public interface GatheringRepository extends JpaRepository<Gathering, Integer>, GatheringRepositoryCustom {

	
}
