package com.dev.moyering.admin.controller;

import com.dev.moyering.admin.dto.PaymentSettlementViewDto;
import com.dev.moyering.admin.service.PaymentSettlementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/unsettlement")
@RequiredArgsConstructor
@Slf4j // 로깅
@CrossOrigin(origins = "*")
public class AdminUnsettlementController {
    private final PaymentSettlementService paymentSettlementService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getPendingSettlements(
            @RequestParam(required = false) String searchKeyword,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @PageableDefault(size = 20) Pageable pageable) {

        try {
            log.info("정산 대기 목록 조회 요청 - 검색어: {}, 기간: {} ~ {}", searchKeyword, startDate, endDate);

            PageImpl<PaymentSettlementViewDto> pendingSettlements = paymentSettlementService
                    .getPendingSettlements(searchKeyword, startDate, endDate, pageable);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "정산 대기 목록 조회 성공");
            response.put("data", pendingSettlements.getContent());
            response.put("totalElements", pendingSettlements.getTotalElements());
            response.put("totalPages", pendingSettlements.getTotalPages());
            response.put("currentPage", pendingSettlements.getNumber());
            response.put("pageSize", pendingSettlements.getSize());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("정산 대기 목록 조회 실패", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "정산 대기 목록 조회 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
}

