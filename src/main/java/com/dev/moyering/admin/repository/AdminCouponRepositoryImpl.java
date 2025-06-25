package com.dev.moyering.admin.repository;

import com.dev.moyering.admin.dto.AdminCouponDto;
import com.dev.moyering.admin.dto.AdminCouponSearchCond;
import com.dev.moyering.admin.entity.CouponStatus;
import com.dev.moyering.admin.entity.QAdminCoupon;
import com.dev.moyering.user.entity.QUserCoupon;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor
public class AdminCouponRepositoryImpl implements AdminCouponRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private static final QAdminCoupon coupon = QAdminCoupon.adminCoupon;
    private static final QUserCoupon userCoupon = QUserCoupon.userCoupon;

    @Override
    public Page<AdminCouponDto> findCouponByCond(AdminCouponSearchCond cond, Pageable pageable) {
        BooleanBuilder builder = buildSearchCondition(cond);

        List<AdminCouponDto> content = queryFactory
                .select(Projections.constructor(AdminCouponDto.class,
                        coupon.couponId,
                        coupon.couponType,
                        coupon.couponCode,
                        coupon.discountType,
                        coupon.discount,
                        coupon.issueCount,
                        coupon.validFrom,
                        coupon.validUntil,
                        coupon.createdAt
                ))
                .from(coupon)
                .where(builder)
                .groupBy(coupon.couponId)
                .orderBy(coupon.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        log.info("QueryDSL 조회 결과: {}개", content.size());

        // 전체 카운트 조회
        long total = queryFactory
                .select(coupon.count())
                .from(coupon)
                .where(builder)
                .fetchOne();

        List<AdminCouponDto> dtoList = content.stream()
                .map(dto -> {
                    // 상태 계산
                    CouponStatus status = CouponStatus.determineCouponStatus(
                            dto.getValidFrom(),
                            dto.getValidUntil(),
                            dto.getIssueCount()
                    );

                    //  상태가 추가된 새 DTO 반환
                    return AdminCouponDto.builder()
                            .couponId(dto.getCouponId())
                            .couponType(dto.getCouponType())
                            .couponCode(dto.getCouponCode())
                            .discountType(dto.getDiscountType())
                            .discount(dto.getDiscount())
                            .issueCount(dto.getIssueCount())
                            .validFrom(dto.getValidFrom())
                            .validUntil(dto.getValidUntil())
                            .createdAt(dto.getCreatedAt())
                            .status(status)  // 상태 설정
                            .build();
                })
//                .filter(dto -> {
//                    // 상태 필터 적용
//                    if (StringUtils.hasText(cond.getStatus()) && !"ALL".equals(cond.getStatus())) {
//                        CouponStatus filterStatus = CouponStatus.valueOf(cond.getStatus());
//                        return dto.getStatus() == filterStatus;
//                    }
//                    return true;
//                })
                .collect(Collectors.toList());
        log.info("최종 반환 결과: {}개", dtoList.size());

        return new PageImpl<>(dtoList, pageable, total);
    }

    private BooleanBuilder buildSearchCondition(AdminCouponSearchCond cond) {
        BooleanBuilder builder = new BooleanBuilder();

        // 검색 키워드 (쿠폰명 또는 쿠폰코드)
        if (StringUtils.hasText(cond.getKeyword())) {
            builder.and(
                coupon.couponCode.containsIgnoreCase(cond.getKeyword())
            );
        }

        // 쿠폰 발급 주체 필터
        if (StringUtils.hasText(cond.getCouponType()) && !"ALL".equals(cond.getCouponType())) {
            builder.and(coupon.couponType.eq(cond.getCouponType()));
        }

        // 날짜 범위 검색
        if (cond.getFromDate() != null) {
            LocalDateTime fromDateTime = cond.getFromDate().toLocalDate().atStartOfDay();
            builder.and(coupon.createdAt.goe(fromDateTime));
        }

        if (cond.getToDate() != null) {
            LocalDateTime toDateTime = cond.getToDate().toLocalDate().atTime(23, 59, 59);
            builder.and(coupon.createdAt.loe(toDateTime));
        }

        return builder;
    }
}

// 쿠폰 상태 : 예정 / 활성 / 만료 / 소진
// 쿠폰 시작 - 종료일자 안에 있으면 '활성'
// 종료일 밖에있으면 => 만료
// 유효기간 안에 있으나 쿠폰 사용이 발급보다 많아지면, 소진
// 쿠폰 시작일이 오늘일자 이후면 '예정'
