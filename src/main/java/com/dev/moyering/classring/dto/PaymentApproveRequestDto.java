package com.dev.moyering.classring.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PaymentApproveRequestDto {
    private String orderNo;
    private Integer amount;
    private String paymentType;
    private Integer calendarId;
    private Integer userCouponId;
}
