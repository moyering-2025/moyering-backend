package com.dev.moyering.gathering.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.moyering.gathering.entity.GatheringInquiry;

public interface GatheringInquiryRepository extends JpaRepository<GatheringInquiry, Integer>, GatheringInquiryRepositoryCustom {

}