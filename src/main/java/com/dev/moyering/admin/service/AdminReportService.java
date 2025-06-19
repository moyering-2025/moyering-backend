package com.dev.moyering.admin.service;


import com.dev.moyering.admin.dto.AdminReportDto;

import com.dev.moyering.admin.entity.ReportProcessStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface AdminReportService {
    // 신고 목록 조회 (검색 + 필터링)
    Page<AdminReportDto> getReports(String keyword, String reportType, String processStatus, Pageable pageable);

    // 신고 처리 (승인/반려) - 처리 버튼 용
    // 원래는 요청받는 데이터 dto를 따로 만드는게 좋으나, 본 프로젝트는 작으므로 ReportDto 사용
    void processReport(Integer reportId, AdminReportDto request) ;
}
