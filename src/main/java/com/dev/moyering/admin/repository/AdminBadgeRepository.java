package com.dev.moyering.admin.repository;

import com.dev.moyering.admin.entity.AdminBadge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminBadgeRepository extends JpaRepository<AdminBadge, Integer> {
}
