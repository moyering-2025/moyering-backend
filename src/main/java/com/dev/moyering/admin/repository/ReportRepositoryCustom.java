// src/main/java/com/dev/moyering/repository/AdminNoticeRepositoryCustom.java
package com.dev.moyering.admin.repository;

import com.dev.moyering.admin.dto.ReportDto;
import com.dev.moyering.admin.entity.ReportProcessStatus;
import com.dev.moyering.admin.entity.ReportType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReportRepositoryCustom {
    // 목록 조회 및 검색
    Page<ReportDto> findReportsByKeyword(String searchKeyword, Pageable pageable);
}