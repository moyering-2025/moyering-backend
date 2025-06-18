package com.dev.moyering.admin.repository;

import com.dev.moyering.admin.entity.AdminReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<AdminReport, Integer> {
}
