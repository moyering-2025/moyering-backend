package com.dev.moyering.admin.entity;


import com.dev.moyering.admin.dto.PaymentSettlementViewDto;
import com.google.errorprone.annotations.Immutable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "payment_settlement")
@Immutable  // 읽기 전용 뷰
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentSettlementView {

    @Id
    @Column(name = "payment_id")
    private Long paymentId;

    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "payer_username")
    private String payerUsername;

    @Column(name = "host_id")
    private Long hostId;

    @Column(name = "class_id")
    private Long classId;

    @Column(name = "class_name")
    private String className;

    @Column(name = "class_price", precision = 15, scale = 2)
    private BigDecimal classPrice;

    @Column(name = "class_date")
    private LocalDate classDate;

    @Column(name = "payment_amount", precision = 15, scale = 2)
    private BigDecimal paymentAmount;

    @Column(name = "payment_type")
    private String paymentType;

    @Column(name = "coupon_type")
    private String couponType;

    @Column(name = "platform_fee", precision = 15, scale = 2)
    private BigDecimal platformFee;

    @Column(name = "settlement_amount", precision = 15, scale = 2)
    private BigDecimal settlementAmount;

    @Column(name = "payment_status")
    private String paymentStatus;

    @Column(name = "class_status")
    private String classStatus;

    /**
     * 엔티티 → DTO 변환
     */
    public PaymentSettlementViewDto toDto() {
        return new PaymentSettlementViewDto(
                this.paymentId,
                this.studentId,
                this.payerUsername,
                this.hostId,
                this.classId,
                this.className,
                this.classPrice,
                this.classDate,
                this.paymentAmount,
                this.paymentType,
                this.couponType,
                this.platformFee,
                this.settlementAmount,
                this.paymentStatus,
                this.classStatus
        );
    }
}