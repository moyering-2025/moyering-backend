package com.dev.moyering.admin.service;
import com.dev.moyering.admin.dto.AdminSettlementDto;
import com.dev.moyering.admin.entity.AdminSettlement;
import com.dev.moyering.admin.repository.AdminSettlementRepository;
import com.dev.moyering.host.dto.SettlementSearchRequestDto;
import com.dev.moyering.user.repository.UserPaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    @Transactional
    @Override
    public boolean completeSettlement(Integer settlementId) {
        try {
            log.info("정산 완료 처리 시작 - settlementId: {}", settlementId);

            // 1. 정산 데이터 조회 (Entity 전체 조회)
            AdminSettlement originalSettlement = adminSettlementRepository.findById(settlementId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 정산 데이터: " + settlementId));

            log.info("조회된 정산 데이터 - settlementId: {}, 예정금액: {}, 현재상태: {}",
                    settlementId,
                    originalSettlement.getSettleAmountToDo(),
                    originalSettlement.getSettlementStatus());

            // 2. 도메인 메서드로 정산 완료 처리 (예정금액을 정산금액에 복사)
            AdminSettlement completedSettlement = originalSettlement.completeSettlement();

            // 3. 저장 (JPA가 ID를 기준으로 UPDATE 수행)
            AdminSettlement savedSettlement = adminSettlementRepository.save(completedSettlement);

            log.info("정산 완료 처리 성공 - settlementId: {}, 예정금액: {}, 정산금액: {}, 상태: {}, 정산일: {}",
                    savedSettlement.getSettlementId(),
                    savedSettlement.getSettleAmountToDo(),
                    savedSettlement.getSettlementAmount(),
                    savedSettlement.getSettlementStatus(),
                    savedSettlement.getSettledAt());

            return true;

        } catch (IllegalArgumentException | IllegalStateException e) {
            log.warn("정산 완료 처리 실패 (비즈니스 규칙 위반) - settlementId: {}, error: {}", settlementId, e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("정산 완료 처리 실패 (시스템 오류) - settlementId: {}, error: {}", settlementId, e.getMessage(), e);
            throw new RuntimeException("정산 완료 처리 중 시스템 오류 발생", e);
        }
    }

	@Override
	public Page<AdminSettlementDto> getHostSettlementList(SettlementSearchRequestDto dto) {
		PageRequest pageable = PageRequest.of(dto.getPage(), dto.getSize());
		Page<AdminSettlement> resultPage = adminSettlementRepository.getHostSettlementList(dto, pageable);
		return resultPage.map(AdminSettlement::toDto);
	}
}
