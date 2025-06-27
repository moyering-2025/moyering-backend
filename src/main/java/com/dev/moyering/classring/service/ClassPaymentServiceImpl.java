package com.dev.moyering.classring.service;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.dev.moyering.classring.dto.ClassPaymentResponseDto;
import com.dev.moyering.classring.dto.UserCouponDto;
import com.dev.moyering.classring.entity.UserCoupon;
import com.dev.moyering.classring.repository.UserCouponRepository;
import com.dev.moyering.host.dto.ClassCalendarDto;
import com.dev.moyering.host.dto.HostDto;
import com.dev.moyering.host.entity.HostClass;
import com.dev.moyering.host.repository.ClassCalendarRepository;
import com.dev.moyering.host.repository.HostClassRepository;
import com.dev.moyering.host.repository.HostRepository;
import com.dev.moyering.user.entity.User;
import com.dev.moyering.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClassPaymentServiceImpl implements ClassPaymentService {
    private final HostClassRepository hostClassRepository;
    private final UserRepository userRepository;
    private final UserCouponRepository userCouponRepository;
    private final ClassCalendarRepository classCalendarRepository;
    private final HostRepository hostRepository;
	@Override
	public ClassPaymentResponseDto getClassPaymentInfo(Integer userId, Integer classId, Integer selectedCalendarId) throws Exception {
		HostClass hostClass = hostClassRepository.findById(classId)
	            .orElseThrow(() -> new RuntimeException("클래스를 찾을 수 없습니다"));

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다"));
        
        ClassCalendarDto ccDto = classCalendarRepository.findById(selectedCalendarId)
        		.orElseThrow(()-> new Exception("해당 날짜의 강의가 없습니다."))
        		.toDto();
        HostDto hostDto =  hostRepository.findById(hostClass.getHost().getHostId())
        		.orElseThrow(()-> new Exception("해당 강사가 없습니다."))
        		.toDto();
        
        // 유저가 아직 사용하지 않은 && 관리자의 모든 쿠폰 && 해당 클래스 쿠폰
        List<UserCoupon> userCoupons = userCouponRepository.findAvailableCoupons(userId, classId);

        List<UserCouponDto> couponDtos = userCoupons.stream()
                .map(uc -> uc.toDto())
                .collect(Collectors.toList());

            return ClassPaymentResponseDto.builder()
            		.hostClass(hostClass.toDto())
            		.startDate(ccDto.getStartDate())
            		.hostName(hostDto.getName())
            		.userCoupons(couponDtos)
            		.build();
	}
	
}
