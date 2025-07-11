package com.dev.moyering.gathering.repository;

import java.sql.Date;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.dev.moyering.gathering.entity.GatheringInquiry;

public interface GatheringInquiryRepository extends JpaRepository<GatheringInquiry, Integer>, GatheringInquiryRepositoryCustom {
	
}