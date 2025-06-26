package com.dev.moyering.user.service;

import com.dev.moyering.admin.dto.AdminPaymentDto;
import com.dev.moyering.admin.dto.AdminPaymentSearchCond;
import com.dev.moyering.user.repository.UserPaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserPaymentServiceImpl implements UserPaymentService {

    private final UserPaymentRepository userPaymentRepository;

    /*** 관리자용 결제 내역 조회 (할인 금액 계산 포함)*/
    public Page<AdminPaymentDto> getPaymentList(AdminPaymentSearchCond cond, Pageable pageable) {
        log.debug("결제 내역 조회 시작 - 조건: {}, 페이지: {}", cond, pageable);

        // 1. Repository에서 원본 데이터 조회
        List<AdminPaymentDto> paymentList = userPaymentRepository.searchPaymentList(cond, pageable);
        Long totalCount = userPaymentRepository.countPaymentList(cond); // 목록 개수 조회

        // 2. 서비스에서 할인 금액 계산 로직 처리
        List<AdminPaymentDto> processedList = paymentList.stream()
                .map(this::calculateDiscountAmount) // 할인금액 계산
                .collect(Collectors.toList());

        log.debug("결제 내역 조회 완료 - 총 {}건", totalCount);
        return new PageImpl<>(processedList, pageable, totalCount);
    }

    /*** 할인 금액 계산 로직
     * @param adminPaymentDto 원본 결제 정보
     * @return 할인 금액이 계산된 결제 정보*/
    private AdminPaymentDto calculateDiscountAmount(AdminPaymentDto adminPaymentDto) {
        // 쿠폰이 없는 경우
        if (adminPaymentDto.getCouponType() == null || adminPaymentDto.getDiscountType() == null) {
            adminPaymentDto.setDiscountAmount(0); // 객세 생성 대신 setter 사용
            log.debug("쿠폰 없는 결제 - 주문번호: {}", adminPaymentDto.getOrderNo());
            return adminPaymentDto;
        }

        // 쿠폰의 원래 할인값 (DB에서 가져온 값)
        int couponDiscountValue = adminPaymentDto.getDiscountAmount();

        // 계산된 실제 할인금액
        int calculatedDiscountAmount;

        switch (adminPaymentDto.getDiscountType()) {
            case "AMT": // 금액 할인: 할인값 그대로 적용
                calculatedDiscountAmount = couponDiscountValue;
                log.debug("금액 할인 적용 - 주문번호: {}, 할인금액: {}",
                        adminPaymentDto.getOrderNo(), calculatedDiscountAmount);
                break;

            case "RT": // 비율 할인: 클래스 원가 * 할인율 / 100
                calculatedDiscountAmount = adminPaymentDto.getClassAmount() * couponDiscountValue / 100;
                log.debug("비율 할인 적용 - 주문번호: {}, 원가: {}, 할인율: {}%, 할인금액: {}",
                        adminPaymentDto.getOrderNo(), adminPaymentDto.getClassAmount(),
                        couponDiscountValue, calculatedDiscountAmount);
                break;

            default:
                log.warn("알 수 없는 할인 타입 - 주문번호: {}, 할인타입: {}",
                        adminPaymentDto.getOrderNo(), adminPaymentDto.getDiscountType());
                calculatedDiscountAmount = 0;
                break;
        }

        log.debug("최종 할인금액 계산 완료 - 주문번호: {}, 원래값: {}, 계산된값: {}, 최종값: {}",
                adminPaymentDto.getOrderNo(), couponDiscountValue, calculatedDiscountAmount);
        return adminPaymentDto;

    }
}