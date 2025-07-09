package com.dev.moyering.gathering.repository;

import java.util.List;
import java.util.Date;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.dev.moyering.gathering.dto.GatheringApplyDto;
import com.dev.moyering.gathering.entity.Gathering;
import com.dev.moyering.gathering.entity.GatheringApply;

public interface GatheringApplyRepository extends JpaRepository<GatheringApply, Integer>, GatheringApplyRepositoryCustom {
    Optional<GatheringApply> findByGatheringGatheringIdAndUserUserId(Integer gatheringId, Integer userId);

}
