package com.dev.moyering.config;

import com.dev.moyering.admin.service.AdminVisitorLogsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 방문자 로그 기록
@Component
@RequiredArgsConstructor
@Slf4j
public class VisitorInterceptor implements HandlerInterceptor {

    private final AdminVisitorLogsService visitorLogsService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            // 방문자 기록시 아래 포함하면, 조회 제외
            String uri = request.getRequestURI();

            if (!uri.startsWith("/static")
                    && !uri.contains("favicon")
                    && !uri.equals("/image")
                    && !uri.startsWith("/iupload")
                    && !uri.contains(".jpg")
                    && !uri.contains(".png")
                    && !uri.contains(".css")
                    && !uri.contains(".js")) {

                visitorLogsService.recordVisit(request);
            }
        } catch (Exception e) {
            log.error("방문자 기록 실패", e);
        }
        return true;
    }
}