package com.dev.moyering.gathering.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.moyering.gathering.dto.GatheringApplyDto;
import com.dev.moyering.gathering.entity.Gathering;
import com.dev.moyering.gathering.entity.GatheringApply;
import com.dev.moyering.gathering.entity.GatheringLikes;

public interface GatheringLikesRepository extends JpaRepository<GatheringLikes, Integer>, GatheringLikesRepositoryCustom {


}
