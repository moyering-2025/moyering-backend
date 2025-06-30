package com.dev.moyering.admin.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
// 정산대기 목록 리스트
public class PaymentSettlementViewDto {
    private Long paymentId;
    private Long studentId;
    private String payerUsername;

    private Long hostId;
    private Long classId;
    private String className;
    private BigDecimal classPrice;
    private LocalDate classDate;

    private BigDecimal paymentAmount;
    private String paymentType;
    private String couponType;

    private BigDecimal platformFee;
    private BigDecimal settlementAmount;

    private String paymentStatus;
    private String classStatus;

    // 정산 대기 리스트 dto
    @QueryProjection
    public PaymentSettlementViewDto(Long paymentId, Long studentId, String payerUsername,
                                    Long hostId, Long classId, String className, BigDecimal classPrice,
                                    LocalDate classDate, BigDecimal paymentAmount, String paymentType,
                                    String couponType, BigDecimal platformFee, BigDecimal settlementAmount,
                                    String paymentStatus, String classStatus) {
        this.paymentId = paymentId;
        this.studentId = studentId;
        this.payerUsername = payerUsername;
        this.hostId = hostId;
        this.classId = classId;
        this.className = className;
        this.classPrice = classPrice;
        this.classDate = classDate;
        this.paymentAmount = paymentAmount;
        this.paymentType = paymentType;
        this.couponType = couponType;
        this.platformFee = platformFee;
        this.settlementAmount = settlementAmount;
        this.paymentStatus = paymentStatus;
        this.classStatus = classStatus;
    }
}

