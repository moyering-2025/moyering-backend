package com.dev.moyering.gathering.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.moyering.gathering.dto.GatheringApplyDto;
import com.dev.moyering.gathering.entity.Gathering;
import com.dev.moyering.gathering.entity.GatheringApply;

public interface GatheringApplyRepository extends JpaRepository<GatheringApply, Integer>, GatheringApplyRepositoryCustom {

}
