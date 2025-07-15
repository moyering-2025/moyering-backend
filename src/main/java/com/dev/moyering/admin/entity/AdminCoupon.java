package com.dev.moyering.admin.entity;
import java.time.LocalDateTime;
import javax.persistence.*;

import com.dev.moyering.admin.dto.AdminCouponDto;
import com.dev.moyering.classring.entity.UserCoupon;

import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor
@ToString(of = {"couponId", "couponType", "couponCode", "discountType", "discount", "issueCount",
"validFrom", "validUntil", "createdAt"})
@Builder

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

    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer issueCount; // 발급매수

    private LocalDateTime validFrom; //쿠폰 시작일

    private LocalDateTime validUntil; //쿠폰 종료일

    @Column(nullable = false)
    private LocalDateTime createdAt; // 생성일

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer usedCount = 0; //사용량 (기본값 0으로 설정)



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
                .usedCount(this.usedCount)
                .build();
    }

    // 생성 시 현재 시간 자동 설정
    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.usedCount == null){
            this.usedCount = 0;
        }
    }


    
    //쿠폰 사용 시 매수 차감
    public void incrementUsedCount() {
    	if (this.usedCount == null) {
            this.usedCount = 1;
        } else {
            this.usedCount++;
        }
	}
}