package com.dev.moyering.admin.entity;
import java.time.LocalDateTime;
import javax.persistence.*;

import com.dev.moyering.admin.dto.AdminCouponDto;
import com.dev.moyering.user.entity.UserCoupon;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(of = {"couponId", "couponType", "couponCode", "discountType", "discount", "issueCount",
"validFrom", "validUntil", "createdAt"})

@Table(name="coupon")
public class AdminCoupon {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer couponId; // 쿠폰 아이디

    @Column(nullable = false)
    private String couponType; // 관리자 쿠폰인지, 강사 쿠폰인지

    @Column(nullable = false)
    private String couponCode; // 쿠폰 코드

    @Column(nullable = false)
    private String discountType; // 비율이면 'RT', 금액할인이면 'AMT'

    @Column(nullable = false)
    private Integer discount; // 예: 10 (%), 5000 (금액)

    private Integer issueCount; // 발급매수

    private LocalDateTime validFrom; //쿠폰 시작일

    private LocalDateTime validUntil; //쿠폰 종료일

    @Column(nullable = false)
    private LocalDateTime createdAt; // 생성일



     // PAYMENT구현 끝나고 usedCount 추가
    @Builder
    public AdminCoupon(Integer couponId, String couponType, String couponCode,
                       String discountType, Integer discount, Integer issueCount, Integer usedCount,
                       LocalDateTime validFrom, LocalDateTime validUntil,
                       LocalDateTime createdAt) {
        this.couponId = couponId;
        this.couponType = couponType;
        this.couponCode = couponCode;
        this.discountType = discountType;
        this.discount = discount;
        this.issueCount = issueCount;
        this.validFrom = validFrom;
        this.validUntil = validUntil;
        this.createdAt = createdAt;
    }

    // Entity -> DTO 변환
    public AdminCouponDto toDto() {
        return AdminCouponDto.builder()
                .couponId(this.couponId)
                .couponType(this.couponType)
                .couponCode(this.couponCode)
                .discountType(this.discountType)
                .discount(this.discount)
                .issueCount(this.issueCount)
                .validFrom(this.validFrom)
                .validUntil(this.validUntil)
                .createdAt(this.createdAt)
                .build();
    }

    // 생성 시 현재 시간 자동 설정
    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }
}