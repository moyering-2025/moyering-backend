package com.dev.moyering.admin.service;

import com.dev.moyering.admin.dto.PaymentSettlementViewDto;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface PaymentSettlementService {
    PageImpl<PaymentSettlementViewDto> getPendingSettlements(String searchKeyword, LocalDate startDate, LocalDate endDate, Pageable pageable) throws Exception;
//    Long getPendingSettlementsCount(String searchKeyword, LocalDate startDate, LocalDate endDate) throws Exception;
//    PaymentSettlementViewDto getSettlementDetailByPaymentId(Long paymentId) throws Exception;
}
