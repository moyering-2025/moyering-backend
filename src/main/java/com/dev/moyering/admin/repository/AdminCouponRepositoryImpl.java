package com.dev.moyering.admin.repository;

import com.dev.moyering.admin.dto.AdminCouponDto;
import com.dev.moyering.admin.dto.AdminCouponSearchCond;
import com.dev.moyering.admin.entity.AdminCoupon;
import com.dev.moyering.admin.entity.CouponStatus;
import com.dev.moyering.admin.entity.QAdminCoupon;
import com.dev.moyering.user.entity.QUserCoupon;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class AdminCouponRepositoryImpl implements AdminCouponRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private static final QAdminCoupon coupon = QAdminCoupon.adminCoupon;

    @Override
    public Page<AdminCouponDto> findCouponByCond(AdminCouponSearchCond cond, Pageable pageable) {

        BooleanBuilder builder = new BooleanBuilder();

        // 검색 키워드 (쿠폰명 또는 쿠폰코드)
        if (StringUtils.hasText(cond.getKeyword())) {
            builder.and(
                    coupon.couponName.containsIgnoreCase(cond.getKeyword())
                            .or(coupon.couponCode.containsIgnoreCase(cond.getKeyword()))
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

        // 쿠폰 데이터 조회
        List<AdminCoupon> content = queryFactory
                .selectFrom(coupon)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(coupon.createdAt.desc())
                .fetch();

        // 전체 카운트 조회
        long total = queryFactory
                .select(coupon.count())
                .from(coupon)
                .where(builder)
                .fetchOne();

        // Entity -> DTO 변환 및 상태 계산
        List<AdminCouponDto> dtoList = content.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        // 상태 필터 적용 (DB에서 필터링하기 어려운 동적 계산 필드)
        if (StringUtils.hasText(cond.getStatus()) && !"ALL".equals(cond.getStatus())) {
            CouponStatus filterStatus = CouponStatus.valueOf(cond.getStatus());
            dtoList = dtoList.stream()
                    .filter(dto -> dto.calculateStatus() == filterStatus)
                    .collect(Collectors.toList());
        }

        return new PageImpl<>(dtoList, pageable, total);
    }

    // Entity -> DTO 변환 (실제 발급/사용 통계 포함)
    private AdminCouponDto convertToDto(AdminCoupon entity) {

        // user_coupon 테이블에서 실제 발급/사용 통계 조회
        CouponUsageStats stats = getCouponUsageStats(entity.getCouponId());

        AdminCouponDto dto = entity.toDto();

        // 동적 계산 필드 설정
        return AdminCouponDto.builder()
                .couponId(dto.getCouponId())
                .couponType(dto.getCouponType())
                .couponCode(dto.getCouponCode())
                .discountType(dto.getDiscountType())
                .discount(dto.getDiscount())
                .issueCount(dto.getIssueCount()) // 최대 발급 가능 수량
                .validFrom(dto.getValidFrom())
                .validUntil(dto.getValidUntil())
                .createdAt(dto.getCreatedAt())
                .couponName(dto.getCouponName())
                .calendar(dto.getCalendar())
                .actualIssuedCount(stats.getActualIssuedCount()) // 실제 발급된 수
                .usedCount(stats.getUsedCount()) // 실제 사용된 수
                .remainingCount(Math.max(0, dto.getIssueCount() - stats.getActualIssuedCount()))
                .status(CouponStatus.determineCouponStatus(
                        dto.getValidFrom(), dto.getValidUntil(),
                        dto.getIssueCount(), stats.getActualIssuedCount(), stats.getUsedCount()))
                .build();
    }

    // user_coupon 테이블에서 실제 발급/사용 통계 조회
    private CouponUsageStats getCouponUsageStats(Integer couponId) {
        QUserCoupon userCoupon = QUserCoupon.userCoupon;

        // 실제 발급된 수 조회
        Long actualIssuedCount = queryFactory
                .select(userCoupon.count())
                .from(userCoupon)
                .where(userCoupon.ucId.eq(couponId))
                .fetchOne();

        // 사용된 수 조회
        Long usedCount = queryFactory
                .select(userCoupon.count())
                .from(userCoupon)
                .where(userCoupon.ucId.eq(couponId)
                        .and(userCoupon.status.eq("사용")))
                .fetchOne();

        return new CouponUsageStats(
                actualIssuedCount != null ? actualIssuedCount.intValue() : 0,
                usedCount != null ? usedCount.intValue() : 0
        );
    }

    // 쿠폰 사용 통계를 담는 내부 클래스
    @Getter
    @AllArgsConstructor
    private static class CouponUsageStats {
        private Integer actualIssuedCount;  // 실제 발급된 수 (user_coupon 테이블 기준)
        private Integer usedCount;          // 실제 사용된 수 (status='사용')
    }
}


// 쿠폰 상태 : 예정 / 활성 / 만료 / 소진
// 쿠폰 시작 - 종료일자 안에 있으면 '활성'
// 종료일 밖에있으면 => 만료
// 유효기간 안에 있으나 쿠폰 사용이 발급보다 많아지면, 소진
// 쿠폰 시작일이 오늘일자 이후면 '예정'
