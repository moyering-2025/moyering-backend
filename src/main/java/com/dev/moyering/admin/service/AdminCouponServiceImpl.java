package com.dev.moyering.admin.service;

import com.dev.moyering.admin.dto.AdminCouponDto;
import com.dev.moyering.admin.dto.AdminCouponSearchCond;
import com.dev.moyering.admin.entity.AdminCoupon;
import com.dev.moyering.admin.repository.AdminCouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AdminCouponServiceImpl implements AdminCouponService {

    private final AdminCouponRepository adminCouponRepository;

    @Override
    public Page<AdminCouponDto> getCouponList(AdminCouponSearchCond cond, Pageable pageable) {
        log.info("쿠폰 목록 조회 - 조건: {}, 페이지: {}", cond, pageable);
        return adminCouponRepository.findCouponByCond(cond, pageable);
    }

    @Override
    public AdminCouponDto getCouponById(Integer couponId) {
        log.info("쿠폰 상세 조회 - ID: {}", couponId);

        AdminCoupon coupon = adminCouponRepository.findById(couponId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 쿠폰입니다. ID: " + couponId));
        return coupon.toDto();
    }

    @Override
    @Transactional
    public AdminCouponDto createCoupon(AdminCouponDto couponDto) {
        log.info("쿠폰 생성 - 데이터: {}", couponDto);

        // 쿠폰 코드 중복 체크
        if (adminCouponRepository.existsByCouponCode(couponDto.getCouponCode())) {
            throw new IllegalArgumentException("이미 존재하는 쿠폰 코드입니다: " + couponDto.getCouponCode());
        }

        // 쿠폰 코드가 없으면 자동 생성
        if (couponDto.getCouponCode() == null || couponDto.getCouponCode().trim().isEmpty()) {
            couponDto = AdminCouponDto.builder()
                    .couponType(couponDto.getCouponType())
                    .couponCode(generateCouponCode())
                    .discountType(couponDto.getDiscountType())
                    .discount(couponDto.getDiscount())
                    .issueCount(couponDto.getIssueCount())
                    .validFrom(couponDto.getValidFrom())
                    .validUntil(couponDto.getValidUntil())
                    .build();
        }

        AdminCoupon savedCoupon = adminCouponRepository.save(couponDto.toEntity());
        log.info("쿠폰 생성 완료 - ID: {}", savedCoupon.getCouponId());

        return savedCoupon.toDto();
    }

    @Override
    @Transactional
    public AdminCouponDto updateCoupon(Integer couponId, AdminCouponDto dto) {
        log.info("쿠폰 수정 - ID: {}, 데이터: {}", couponId, dto);

        AdminCoupon existingCoupon = adminCouponRepository.findById(couponId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 쿠폰입니다. ID: " + couponId));

        // 다른 쿠폰과 코드 중복 체크
        if (!existingCoupon.getCouponCode().equals(dto.getCouponCode()) &&
                adminCouponRepository.existsByCouponCode(dto.getCouponCode())) {
            throw new IllegalArgumentException("이미 존재하는 쿠폰 코드입니다: " + dto.getCouponCode());
        }

        // 업데이트할 쿠폰 생성 (ID와 생성일시는 기존 값 유지)
        AdminCoupon updatedCoupon = AdminCoupon.builder()
                .couponId(existingCoupon.getCouponId())
                .couponType(dto.getCouponType())
                .couponCode(dto.getCouponCode())
                .discountType(dto.getDiscountType())
                .discount(dto.getDiscount())
                .issueCount(dto.getIssueCount())
                .validFrom(dto.getValidFrom())
                .validUntil(dto.getValidUntil())
                .createdAt(existingCoupon.getCreatedAt()) // 기존 생성일시 유지
                .build();

        AdminCoupon savedCoupon = adminCouponRepository.save(updatedCoupon);
        log.info("쿠폰 수정 완료 - ID: {}", savedCoupon.getCouponId());

        return savedCoupon.toDto();
    }

    @Override
    @Transactional
    public AdminCouponDto deleteCoupon(Integer couponId) {
        log.info("쿠폰 삭제 - ID: {}", couponId);

        if (!adminCouponRepository.existsById(couponId)) {
            throw new IllegalArgumentException("존재하지 않는 쿠폰입니다. ID: " + couponId);
        }

        // TODO: 쿠폰 사용 이력이 있는지 확인 후 삭제 가능 여부 판단
        adminCouponRepository.deleteById(couponId);
        log.info("쿠폰 삭제 완료 - ID: {}", couponId);
        return null;
    }


    // 쿠폰 코드 자동 생성
    private String generateCouponCode() {
        String prefix = "COUP";
        String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        return prefix + uuid;
    }
}