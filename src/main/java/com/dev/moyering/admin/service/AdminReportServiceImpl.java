package com.dev.moyering.admin.service;

import com.dev.moyering.admin.dto.AdminReportDto;
import com.dev.moyering.admin.repository.AdminReportRepository;

import com.dev.moyering.admin.repository.AdminReportRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j

public class AdminReportServiceImpl implements AdminReportService {
    private final AdminReportRepository reportRepository;
    private final AdminReportRepositoryCustom reportRepositoryCustom;

//    // 각 콘텐츠 타입별 서비스 주입
//    private final PostService postService;
//    private final CommentService commentService;
//    private final UserService userService;
//    private final ClassService classService; // 강의 서비스

    // 신고관리 목록 불러오기 (검색 + 필터)


    @Override
    public Page<AdminReportDto> getReports(String keyword, String reportType, String processStatus, Pageable pageable) {
        // ReportRepositoryCustom의 구현체를 직접 주입받아 사용
        return reportRepositoryCustom.findReportsByKeyword(keyword, pageable);
    }

    @Override
    public void processReport(Integer reportId, AdminReportDto request) {

    }



//    @Override
//    public void processReport(Integer reportId, ReportDto request) {

    }
