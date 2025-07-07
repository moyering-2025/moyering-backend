package com.dev.moyering.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.moyering.admin.entity.AdminNotice;

public interface NoticeRepository extends JpaRepository<AdminNotice, Integer> ,NoticeRepositoryCustom{
	
}
