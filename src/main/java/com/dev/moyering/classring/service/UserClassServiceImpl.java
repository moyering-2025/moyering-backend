package com.dev.moyering.classring.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dev.moyering.classring.dto.UserCouponDto;
import com.dev.moyering.common.dto.ClassRingDetailResponseDto;
import com.dev.moyering.host.dto.ClassCalendarDto;
import com.dev.moyering.host.dto.ClassCouponDto;
import com.dev.moyering.host.dto.ClassRegistDto;
import com.dev.moyering.host.dto.HostClassDto;
import com.dev.moyering.host.dto.HostDto;
import com.dev.moyering.host.dto.ReviewDto;
import com.dev.moyering.host.dto.ScheduleDetailDto;
import com.dev.moyering.host.service.ClassCalendarService;
import com.dev.moyering.host.service.ClassCouponService;
import com.dev.moyering.host.service.ClassRegistService;
import com.dev.moyering.host.service.HostClassService;
import com.dev.moyering.host.service.HostService;
import com.dev.moyering.host.service.ReviewService;
import com.dev.moyering.host.service.ScheduleDetailService;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class UserClassServiceImpl implements UserClassService {
    private final HostClassService hostClassService;
    private final ClassCalendarService classCalendarService;
    private final HostService hostService;
    private final ReviewService reviewService;
    private final ClassCouponService classCouponService;
    private final UserCouponService userCouponService;
    private final ScheduleDetailService scheduleDetailService;
    private final ClassRegistService classRegistService;
	@Override
	public ClassRingDetailResponseDto getClassRingDetail(Integer classId, Integer userId) throws Exception {
		HostClassDto     hostclass    = hostClassService.getClassDetailByClassID(classId);
        List<ClassCalendarDto> classCalendar = classCalendarService.getClassCalendarByHostClassId(classId);
        HostDto          host         = hostService.getHostById(hostclass.getHostId());
        List<ReviewDto>  reviews      = reviewService.getReviewByHostId(hostclass.getHostId());
        List<ClassCouponDto> classCoupons = classCouponService.getCouponByClassId(classId);
        List<ScheduleDetailDto> detailDtos = scheduleDetailService.getScheduleByClassId(classId);
        List<HostClassDto> recommends = hostClassService.getRecommendClassesInDetail(hostclass.getSubCategoryId(),classId);
        List<ClassRegistDto> registeds = classRegistService.getRegisterdClassByUserId(userId);
        
        List<UserCouponDto> userCoupons = null;
        // ① 이미 다운로드한 쿠폰만
        if (userId != null) {
        	userCoupons  = userCouponService.getByUserIdAndClassId(userId, classId);
        }

        return ClassRingDetailResponseDto.builder()
            .hostClass(hostclass)
            .calendarList(classCalendar)
            .currList(classCalendar)      // currList가 다르면 분리해 주세요
            .host(host)
            .reviews(reviews)
            .coupons(classCoupons)
            .userCoupons(userCoupons)
            .detailDtos(detailDtos)
            .recommends(recommends)
            .registeds(registeds)
            .build();
    }

}
