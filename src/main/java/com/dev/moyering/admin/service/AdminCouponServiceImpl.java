package com.dev.moyering.admin.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.dev.moyering.classring.entity.UserCoupon;
import com.dev.moyering.classring.repository.UserCouponRepository;
import com.dev.moyering.user.entity.User;
import com.dev.moyering.user.repository.UserRepository;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dev.moyering.admin.dto.AdminCouponDto;
import com.dev.moyering.admin.dto.AdminCouponSearchCond;
import com.dev.moyering.admin.entity.AdminCoupon;
import com.dev.moyering.admin.repository.AdminCouponRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.dev.moyering.classring.entity.QUserCoupon.userCoupon;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AdminCouponServiceImpl implements AdminCouponService {

    private final AdminCouponRepository adminCouponRepository;
    private final UserRepository userRepository;
    private final UserCouponRepository userCouponRepository;

    @Override
    public Page<AdminCouponDto> getCouponList(AdminCouponSearchCond cond, Pageable pageable) {
        return adminCouponRepository.findCouponByCond(cond, pageable);
    }

    @Override
    public AdminCouponDto getCouponById(Integer couponId) {
        AdminCoupon coupon = adminCouponRepository.findById(couponId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 쿠폰입니다. ID: " + couponId));
        return coupon.toDto();
    }

    @Override
    @Transactional
    public AdminCouponDto createCoupon(AdminCouponDto couponDto) {
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

        // 관리자 쿠폰인 경우, 모든 활성 사용자에게 자동 지급
        if ("MG".equals(couponDto.getCouponType())) {
            try {
                distributeCouponToActiveUsers(savedCoupon.getCouponId());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return savedCoupon.toDto();
    }

    @Override
    @Transactional
    public AdminCouponDto updateCoupon(Integer couponId, AdminCouponDto dto) {
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
        return savedCoupon.toDto();
    }

    @Override
    @Transactional
    public AdminCouponDto deleteCoupon(Integer couponId) {
        if (!adminCouponRepository.existsById(couponId)) {
            throw new IllegalArgumentException("존재하지 않는 쿠폰입니다. ID: " + couponId);}
        adminCouponRepository.deleteById(couponId);
        log.info("쿠폰 삭제 완료 - ID: {}", couponId);
        return null;}


    // 쿠폰 코드 자동 생성
    private String generateCouponCode() {
        String prefix = "COUP";
        String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        return prefix + uuid;}


	@Override
	public List<AdminCouponDto> selectHostAllCoupon(String type) throws Exception {
		List<AdminCoupon> couponList = adminCouponRepository.findAll();
		List<AdminCouponDto> couponDtoList = new ArrayList<>();
		for(AdminCoupon coupon : couponList) {
			if(coupon.getCouponType().equals(type))
			couponDtoList.add(coupon.toDto());
        }
		return couponDtoList;
	}

    // 쿠폰 지급을 위한 활성사용자 조회
    @Override
    public List<User> distributeCouponToActiveUsers(Integer couponId) {
        try {
            // use_yn이 'Y'인 활성 사용자들 조회
            List<User> allUsers = userRepository.findAll();
            List<User> activeUsers = allUsers.stream()
                    .filter(user -> "Y".equals(user.getUseYn()))
                    .collect(Collectors.toList());


            List<UserCoupon> userCoupons = activeUsers.stream()
                    .map(user -> UserCoupon.builder()
                            .user(user)
                            .adminCoupon(AdminCoupon.builder().couponId(couponId).build())
                            .status("미사용")  // 초기 상태
                            .downloadedAt(LocalDateTime.now())  // 다운로드 시간
                            .build())
                    .collect(Collectors.toList());

            userCouponRepository.saveAll(userCoupons);
            return activeUsers;
        } catch (Exception e) {
            System.out.println("erorr");
            throw new RuntimeException(e);
        }
    }

}