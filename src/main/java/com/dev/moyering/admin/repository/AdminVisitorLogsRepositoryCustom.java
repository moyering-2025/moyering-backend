package com.dev.moyering.admin.repository;

import org.springframework.stereotype.Repository;

import java.time.LocalDate;

public interface AdminVisitorLogsRepositoryCustom {
    // 중복 체크 (오늘 이미 기록했는지)
    boolean existsBySessionIdAndVisitDate(String sessionId, LocalDate visitDate);

    // 오늘 방문자 수
    long countByVisitDate(LocalDate visitDate);

    // 이번 달 방문자 수 (MAU)
    long countByVisitDateBetween(LocalDate startDate, LocalDate endDate);

    // 오늘 회원/비회원 수
    long countByVisitDateAndMemberYn(LocalDate date, Boolean isMember);
}