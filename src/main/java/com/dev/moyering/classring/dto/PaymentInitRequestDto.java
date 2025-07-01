package com.dev.moyering.classring.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentInitRequestDto {
    private String orderNo;
    private Integer calendarId;
    private Integer userCouponId;
    private Integer amount;
    private String paymentType;
}
