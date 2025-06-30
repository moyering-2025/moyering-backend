package com.dev.moyering.admin.service;

import com.dev.moyering.admin.dto.AdminSettlementDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface AdminSettlementService {
    // 조건에 맞는 정산 리스트
    Page<AdminSettlementDto> getSettlementList(String searchKeyword, LocalDate startDate, LocalDate endDate, Pageable pageable) throws Exception;

    AdminSettlementDto getSettlementById(Integer settlementId) throws Exception;
//
//    // 정산처리 메서드
////    void processSettlement(Integer settlementId) throws Exception;



}
