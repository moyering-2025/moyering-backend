package com.dev.moyering.admin.repository;

import com.dev.moyering.admin.entity.VisitorLogs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminVisitorLogsRepository extends JpaRepository<VisitorLogs, Integer>, AdminVisitorLogsRepositoryCustom {

}
