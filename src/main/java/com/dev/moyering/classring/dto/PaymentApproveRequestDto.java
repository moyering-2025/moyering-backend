package com.dev.moyering.classring.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentApproveRequestDto {
    private String orderNo;
    private Integer amount;
    private String paymentType;
    private Integer calendarId;
    private Integer userCouponId;
}
