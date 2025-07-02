package com.dev.moyering.classring.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClassPaymentRequestDto {
    private Integer calendarId;
    private Integer userCouponId;
    private String orderNo;
    private Integer amount;
    private String paymentType;
}
