package com.dev.moyering.admin.controller;

import com.dev.moyering.admin.dto.AdminPaymentDto;
import com.dev.moyering.admin.dto.AdminPaymentSearchCond;
import com.dev.moyering.user.service.UserPaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
@Slf4j // 로깅
@CrossOrigin(origins = "*")
public class AdminPaymentController {

    private final UserPaymentService userPaymentService;

    /*** 관리자 결제관리 조회*/
    @GetMapping
    public ResponseEntity<Page<AdminPaymentDto>> getPaymentList (
            AdminPaymentSearchCond cond,
            @PageableDefault(size = 20, sort = "paidAt", direction = Sort.Direction.DESC) Pageable pageable){

        log.info("결제 내역 조회 요청 : keyword ={}, page={}", cond, pageable);

        try {
            Page<AdminPaymentDto> payments = userPaymentService.getPaymentList(cond, pageable);
            return ResponseEntity.ok(payments);
        } catch(Exception e) {
            log.error("결제 목록 조회 요청 오류 : {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
