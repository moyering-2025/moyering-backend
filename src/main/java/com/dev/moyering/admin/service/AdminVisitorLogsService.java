package com.dev.moyering.admin.service;

import com.dev.moyering.admin.dto.VisitorLogsDto;

import javax.servlet.http.HttpServletRequest;

public interface AdminVisitorLogsService {

    /*** 방문 기록 (하루에 한 번만)*/
    void recordVisit(HttpServletRequest request);

//    /***오늘 방문자 통계*/
//    VisitorLogsDto getTodayStats();
//
//    /*** 이번 달 방문자 수 (MAU)*/
//    long getMonthlyVisitorCount();
}