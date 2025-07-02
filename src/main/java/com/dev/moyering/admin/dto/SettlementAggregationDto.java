package com.dev.moyering.admin.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class SettlementAggregationDto {
        private Integer hostId;
        private Integer classId;
        private String className;
        private LocalDate classDate;
        private BigDecimal totalIncome;      // 총 수입
        private BigDecimal totalPlatformFee; // 총 수수료
        private BigDecimal totalSettlementAmount; // 총 정산 금액

        public SettlementAggregationDto(Integer hostId, Integer classId, String className,
                                        LocalDate classDate, BigDecimal totalIncome,
                                        BigDecimal totalPlatformFee, BigDecimal totalSettlementAmount) {
            this.hostId = hostId;
            this.classId = classId;
            this.className = className;
            this.classDate = classDate;
            this.totalIncome = totalIncome;
            this.totalPlatformFee = totalPlatformFee;
            this.totalSettlementAmount = totalSettlementAmount;
        }
}