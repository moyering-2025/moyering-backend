package com.dev.moyering.admin.repository;

import com.dev.moyering.admin.dto.PaymentSettlementViewDto;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface AdminSettlementQueryRepositoryCustom  {
    /*** 정산 대기 목록 조회 (수업 종료 + 7일 후, 아직 정산되지 않은 것들)*/
    List<PaymentSettlementViewDto> getPendingSettlements(String searchKeyword,
                                                         LocalDate startDate,LocalDate endDate,Pageable pageable
                                                         );

    /*** 정산 대기 목록 총 개수*/
    Long getPendingSettlementsCount(String searchKeyword,
                                    LocalDate startDate,
                                    LocalDate endDate);

    /*** 특정 결제의 정산 상세 정보 조회*/
    PaymentSettlementViewDto getSettlementDetailByPaymentId(Long paymentId);
}

