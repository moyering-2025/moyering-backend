package com.dev.moyering.host.dto;

import com.dev.moyering.host.entity.CouponPolicy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ClassCouponDto {
    private Integer classCouponId;
    private CouponPolicy policy;
    private Integer calendarId;
    private String couponName;
    private String amount;
}
