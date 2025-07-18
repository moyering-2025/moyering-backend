package com.dev.moyering.admin.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dev.moyering.admin.dto.AdminPaymentDto;
import com.dev.moyering.admin.dto.AdminSettlementDto;
import com.dev.moyering.admin.entity.AdminSettlement;
import com.dev.moyering.admin.repository.AdminSettlementRepository;
import com.dev.moyering.common.dto.AlarmDto;
import com.dev.moyering.common.service.AlarmService;
import com.dev.moyering.host.dto.SettlementSearchRequestDto;
import com.dev.moyering.host.entity.Host;
import com.dev.moyering.host.repository.HostRepository;
import com.dev.moyering.user.repository.UserPaymentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AdminSettlementServiceImpl implements AdminSettlementService {
	private final AdminSettlementRepository adminSettlementRepository;
	private final UserPaymentRepository userPaymentRepository;
	private final AlarmService alarmService;
	private final HostRepository hostRepository;

	@Override
	public Optional<AdminSettlementDto> getSettlementBySettlementId(Integer settlementId) {
		log.debug("getSettlementBySettlementId {}", settlementId);
		return adminSettlementRepository.getSettlementBySettlementId(settlementId);
	}

	@Override
	public Page<AdminSettlementDto> getSettlementList(String searchKeyword, LocalDate startDate, LocalDate endDate,
			Pageable pageable) {
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
	public boolean completeSettlement(Integer settlementId, Integer totalSettlementAmount) {
		try {
			log.info("정산 완료 처리 시작 - settlementId: {}, 받은금액: {}", settlementId, totalSettlementAmount);

			// 1. 정산 데이터 조회 (Entity 전체 조회)
			AdminSettlement originalSettlement = adminSettlementRepository.findById(settlementId)
					.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 정산 데이터: " + settlementId));

			log.info("조회된 정산 데이터 - settlementId: {}, 예정금액: {}, 현재상태: {}", settlementId,
					originalSettlement.getSettleAmountToDo(), originalSettlement.getSettlementStatus());

			// 2. 프론트에서 받은 금액으로 직접 설정
			originalSettlement.setSettlementAmount(totalSettlementAmount != null ? totalSettlementAmount : 0);
			originalSettlement.setSettlementStatus("CP");
			originalSettlement.setSettledAt(LocalDateTime.now());

			// 3. 저장 (JPA가 ID를 기준으로 UPDATE 수행)
			AdminSettlement savedSettlement = adminSettlementRepository.save(originalSettlement);

			log.info("정산 완료 처리 성공 - settlementId: {}, 받은금액: {}, 저장된정산금액: {}, 상태: {}, 정산일: {}",
					savedSettlement.getSettlementId(), totalSettlementAmount, savedSettlement.getSettlementAmount(),
					savedSettlement.getSettlementStatus(), savedSettlement.getSettledAt());

			// 4. 알림 발송 (savedSettlement 사용하여 중복 조회 방지)
			Host host = hostRepository.findById(savedSettlement.getHostId())
					.orElseThrow(() -> new IllegalArgumentException("강사 정보 없음: " + savedSettlement.getHostId()));

			AlarmDto alarmDto = AlarmDto.builder().alarmType(2) // '1: 시스템,관리자 알람 2 : 클래스링 알람, 3 : 게더링 알람, 4: 소셜링 알람'
					.title("정산 완료 안내") // 필수 사항
					.receiverId(host.getUserId()) // 수신자 유저 아이디
					.senderId(4) // 발신자 유저 아이디
					.senderNickname("admin") // 발신자 닉네임
					.content(host.getName() + "님의 정산처리가 완료되었습니다.") // 알림 내용
					.build();

			alarmService.sendAlarm(alarmDto);

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

	/*** 특정 정산의 결제 내역 조회 (할인 금액 계산 포함) */
	public List<AdminPaymentDto> getPaymentListBySettlementId(Integer settlementId) {
		log.info("정산 결제 내역 조회 - settlementId: {}", settlementId);

		try {
			// 1. Repository에서 원본 데이터 조회
			List<AdminPaymentDto> payments = adminSettlementRepository.getPaymentListBySettlementId(settlementId);

			if (payments.isEmpty()) {
				log.warn("정산 ID {}에 해당하는 결제 내역이 없습니다.", settlementId);
				return Collections.emptyList();
			}

			// 2. 각 결제에 대해 할인 금액 계산 (기존 UserPaymentService 로직 활용)
			List<AdminPaymentDto> processedPayments = payments.stream().map(this::calculateDiscountAmount)
					.collect(Collectors.toList());

			log.info("정산 결제 내역 처리 완료 - settlementId: {}, 건수: {}", settlementId, processedPayments.size());

			return processedPayments;

		} catch (Exception e) {
			log.error("정산 결제 내역 조회 실패 - settlementId: {}, error: {}", settlementId, e.getMessage());
			throw new RuntimeException("정산 결제 내역 조회에 실패했습니다.", e);
		}
	}

	/*** 할인 금액 계산 로직 (기존 UserPaymentService와 동일) */
	private AdminPaymentDto calculateDiscountAmount(AdminPaymentDto payment) {
		// 쿠폰이 없는 경우
		if (payment.getCouponType() == null || payment.getDiscountType() == null) {
			payment.setCalculatedDiscountAmount(0);
			log.debug("쿠폰 없는 결제 - 주문번호: {}", payment.getOrderNo());
			return payment;
		}

		int couponDiscountValue = payment.getDiscountAmount();
		int calculatedDiscountAmount;

		switch (payment.getDiscountType()) {
		case "AMT": // 금액 할인
			calculatedDiscountAmount = couponDiscountValue;
			log.debug("금액 할인 적용 - 주문번호: {}, 할인금액: {}", payment.getOrderNo(), calculatedDiscountAmount);
			break;

		case "RT": // 비율 할인
			calculatedDiscountAmount = payment.getClassAmount() * couponDiscountValue / 100;
			log.debug("비율 할인 적용 - 주문번호: {}, 원가: {}, 할인율: {}%, 할인금액: {}", payment.getOrderNo(), payment.getClassAmount(),
					couponDiscountValue, calculatedDiscountAmount);
			break;

		default:
			log.warn("알 수 없는 할인 타입 - 주문번호: {}, 할인타입: {}", payment.getOrderNo(), payment.getDiscountType());
			calculatedDiscountAmount = 0;
			break;
		}

		payment.setCalculatedDiscountAmount(calculatedDiscountAmount);
		return payment;
	}

	@Transactional
	@Override
	public void settelementRequest(Integer settlementId) {
		AdminSettlement settle = adminSettlementRepository.findById(settlementId)
				.orElseThrow(() -> new IllegalArgumentException("정산 항목 없음"));
		settle.setSettlementStatus("RQ");
		System.out.println("상태변경됨: " + settle);
	}
}