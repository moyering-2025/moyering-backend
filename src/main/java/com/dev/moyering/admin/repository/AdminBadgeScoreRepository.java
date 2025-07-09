package com.dev.moyering.admin.repository;

import com.dev.moyering.admin.entity.AdminBadgeScore;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminBadgeScoreRepository extends JpaRepository<AdminBadgeScore, Integer> {
	Optional<AdminBadgeScore> findByTitle(String title)throws Exception;
}
