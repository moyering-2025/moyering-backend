package com.dev.moyering.admin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.moyering.admin.entity.Banner;

public interface BannerRepository extends JpaRepository<Banner, Integer>, BannerRepositoryCustom{
	List<Banner> findByStatus(Integer status);
}
