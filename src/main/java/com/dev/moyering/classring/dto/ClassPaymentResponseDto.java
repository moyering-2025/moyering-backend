package com.dev.moyering.classring.dto;

import java.sql.Date;
import java.util.List;

import com.dev.moyering.common.dto.ClassRingDetailResponseDto;
import com.dev.moyering.host.dto.ClassCalendarDto;
import com.dev.moyering.host.dto.ClassCouponDto;
import com.dev.moyering.host.dto.HostClassDto;
import com.dev.moyering.host.dto.HostDto;
import com.dev.moyering.host.dto.ReviewDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClassPaymentResponseDto {
    private HostClassDto hostClass;
    private Date startDate;
    private String hostName;
    private List<UserCouponDto> userCoupons;
    
}
