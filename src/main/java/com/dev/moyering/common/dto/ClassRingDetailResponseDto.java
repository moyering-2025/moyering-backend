package com.dev.moyering.common.dto;

import java.util.List;

import com.dev.moyering.classring.dto.UserCouponDto;
import com.dev.moyering.host.dto.ClassCalendarDto;
import com.dev.moyering.host.dto.ClassCouponDto;
import com.dev.moyering.host.dto.ClassRegistDto;
import com.dev.moyering.host.dto.HostClassDto;
import com.dev.moyering.host.dto.HostDto;
import com.dev.moyering.host.dto.ReviewDto;
import com.dev.moyering.host.dto.ScheduleDetailDto;

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
public class ClassRingDetailResponseDto {
    private HostClassDto hostClass;
    private List<ClassCalendarDto> calendarList;
    private List<ClassCalendarDto> currList;
    private HostDto host;
    private List<ReviewDto> reviews;
    private List<ClassCouponDto> coupons;
    private List<UserCouponDto> userCoupons;
    private List<ScheduleDetailDto> detailDtos;
    private List<HostClassDto> recommends;
    private List<ClassRegistDto> registeds;
}
