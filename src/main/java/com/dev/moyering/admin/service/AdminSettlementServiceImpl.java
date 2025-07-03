package com.dev.moyering.admin.service;
import com.dev.moyering.admin.dto.AdminSettlementDto;
import com.dev.moyering.admin.repository.AdminSettlementRepository;
import com.dev.moyering.user.repository.UserPaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AdminSettlementServiceImpl implements AdminSettlementService {
    private final AdminSettlementRepository adminSettlementRepository;
    private final UserPaymentRepository userPaymentRepository;


    @Override
    public Optional<AdminSettlementDto> getSettlementBySettlementId(Integer settlementId) {
        log.debug("getSettlementBySettlementId {}", settlementId);
        return adminSettlementRepository.getSettlementBySettlementId(settlementId);
    }

    @Override
    public Page<AdminSettlementDto> getSettlementList(String searchKeyword, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        log.debug("getSettlementList {}", searchKeyword);
        return adminSettlementRepository.getSettlementList(searchKeyword, startDate, endDate, pageable);
    }

    @Override
    public Long getSettlementListCount(String searchKeyword, LocalDate startDate, LocalDate endDate) {
        log.debug("getSettlementListCount {}", searchKeyword);
        return adminSettlementRepository.getSettlementListCount(searchKeyword, startDate, endDate);
    }


    }

//    @Override
//    public Page <PaymentSettlementViewDto> getPendingSettlementList(String searchKeyword, LocalDate startDate, LocalDate endDate, Pageable pageable) {
//        log.debug("getPendingSettlementList {}", searchKeyword);
//        return adminSettlementRepository.getPendingSettlementList(searchKeyword, startDate, endDate, pageable);
//    }
//
//    @Override
//    public Long getPendingSettlementListCount(String searchKeyword, LocalDate startDate, LocalDate endDate) {
//        log.debug("getPendingSettlementListCount {}", searchKeyword);
//        return adminSettlementRepository.getPendingSettlementListCount(searchKeyword, startDate, endDate);
//    }
//
//    @Override
//    public Optional<SettlementAggregationDto> getSettlementAggregationByClass(Long classId) {
//        log.debug("getSettlementAggregationByClass {}", classId);
//        return adminSettlementRepository.getSettlementAggregationByClass(classId);
//    }
