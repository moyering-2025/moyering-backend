package com.dev.moyering.host.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class CouponPolicyDto {
	private Integer policyId;
	private String couponType;
	private Integer discount;
	private Boolean active;
	private LocalDateTime createdAt;
}
