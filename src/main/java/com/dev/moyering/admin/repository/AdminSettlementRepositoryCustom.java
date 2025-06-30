package com.dev.moyering.admin.repository;

import com.dev.moyering.admin.dto.AdminSettlementDto;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AdminSettlementRepositoryCustom {
    // 단건 조회
    Optional <AdminSettlementDto> getSettlementListBySettlementId (Integer settlementId);

    // 조건에 맞는 리스트 가져오기 (검색 + 날짜 + 페이징)
    List<AdminSettlementDto> getSettlementList(String searchKeyword, LocalDate startDate, LocalDate endDate, Pageable pageable);

    // 페이징을 위한 전체 개수 조회
    Long getSettlementListCount(String searchKeyword, LocalDate startDate, LocalDate endDate);

}
