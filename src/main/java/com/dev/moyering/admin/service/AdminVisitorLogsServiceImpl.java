package com.dev.moyering.admin.service;

import com.dev.moyering.admin.dto.VisitorLogsDto;
import com.dev.moyering.admin.entity.VisitorLogs;
import com.dev.moyering.admin.repository.AdminVisitorLogsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminVisitorLogsServiceImpl implements AdminVisitorLogsService {

    private final AdminVisitorLogsRepository visitorLogsRepository;

    @Override
    public void recordVisit(HttpServletRequest request) {
        try {
            String sessionId = request.getSession().getId(); // 세션 ID
            String ipAddress = request.getRemoteAddr();      // IP 주소
            Integer userId = getCurrentUserId(request);       // 로그인 사용자 ID
            LocalDate today = LocalDate.now();

            // DB 조회 전 세션에서 확인
            String todayKey = "visited_" + today.toString();
            if (request.getSession().getAttribute(todayKey) != null) {
                return; // 이미 기록했으면 리턴!
            }

            log.info(" 요청 URI: {}", request.getRequestURI());
            log.info(" 세션 ID: {}", sessionId);
            log.info(" 오늘 기록 존재 여부: {}", visitorLogsRepository.existsBySessionIdAndVisitDate(sessionId, today));


            // DB조회 전 오늘 이미 기록했는지 체크
            if (!visitorLogsRepository.existsBySessionIdAndVisitDate(sessionId, today)) {
                VisitorLogs visitorLog = VisitorLogs.builder()
                        .userId(userId)
                        .sessionId(sessionId)
                        .ipAddress(ipAddress)
                        .visitDate(today)
                        .visitTime(LocalDateTime.now())
                        .memberYn(userId != null)
                        .build();

                visitorLogsRepository.save(visitorLog);
                log.info("방문 기록: 세션={}, 회원={}", sessionId, userId != null);
            }
        } catch (Exception e) {
            log.error("방문 기록 실패", e);
        }
    }

//    @Override
//    public VisitorLogsDto getTodayStats() {
//        LocalDate today = LocalDate.now();
//
//        long totalCount = visitorLogsRepository.countByVisitDate(today);
//        long memberCount = visitorLogsRepository.countByVisitDateAndMemberYn(today, true);
//        long guestCount = visitorLogsRepository.countByVisitDateAndMemberYn(today, false);
//
//        return VisitorLogsDto.builder()
//                .visitDate(today)
//                .visitorCount(totalCount)
//                .memberCount(memberCount)
//                .guestCount(guestCount)
//                .build();
//    }
//
//    @Override
//    public long getMonthlyVisitorCount() {
//        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
//        LocalDate endOfMonth = LocalDate.now();
//
//        return visitorLogsRepository.countByVisitDateBetween(startOfMonth, endOfMonth);
//    }

    /**
     * 현재 사용자 ID 가져오기 (private 메서드)
     */
    private Integer getCurrentUserId(HttpServletRequest request) {
        // TODO: JWT나 세션에서 사용자 ID 추출
        // 지금은 일단 null (비회원으로 처리)
        return null;
    }
}