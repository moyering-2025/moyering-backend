package com.dev.moyering.classring.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dev.moyering.admin.entity.AdminCoupon;
import com.dev.moyering.admin.repository.AdminCouponRepository;
import com.dev.moyering.classring.dto.ClassPaymentResponseDto;
import com.dev.moyering.classring.dto.PaymentApproveRequestDto;
import com.dev.moyering.classring.dto.PaymentInitRequestDto;
import com.dev.moyering.classring.dto.UserCouponDto;
import com.dev.moyering.classring.entity.UserCoupon;
import com.dev.moyering.classring.repository.UserCouponRepository;
import com.dev.moyering.host.dto.ClassCalendarDto;
import com.dev.moyering.host.dto.HostDto;
import com.dev.moyering.host.entity.ClassCalendar;
import com.dev.moyering.host.entity.ClassCoupon;
import com.dev.moyering.host.entity.ClassRegist;
import com.dev.moyering.host.entity.HostClass;
import com.dev.moyering.host.repository.ClassCalendarRepository;
import com.dev.moyering.host.repository.ClassCouponRepository;
import com.dev.moyering.host.repository.ClassRegistRepository;
import com.dev.moyering.host.repository.HostClassRepository;
import com.dev.moyering.host.repository.HostRepository;
import com.dev.moyering.user.entity.User;
import com.dev.moyering.user.entity.UserPayment;
import com.dev.moyering.user.repository.UserPaymentRepository;
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
    private final ClassRegistRepository classRegistRepository;
    private final UserPaymentRepository userPaymentRepository; 
    private final AdminCouponRepository adminCouponRepository;
    private final ClassCouponRepository classCouponRepository;
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
	
	@Transactional
	@Override
	public void approvePayment(PaymentApproveRequestDto dto, User user) throws Exception {
		// 1. 수강 정보 등록 (class_regist)
	    ClassCalendar cc = classCalendarRepository.findById(dto.getCalendarId())
	            .orElseThrow(() -> new RuntimeException("일정을 찾을 수 없습니다."));
		ClassRegist regist = ClassRegist.builder()
            .user(user)
            .classCalendar(cc)
            .attCount(0)
            .build();
        regist = classRegistRepository.save(regist);
        
        // 2. 결제 정보 저장 (payment)
        UserPayment payment = userPaymentRepository.findByOrderNo(dto.getOrderNo())
                .orElseThrow(() -> new Exception("해당 주문번호의 결제 정보가 존재하지 않습니다."));
        
        if (!payment.getAmount().equals(dto.getAmount())) {
            throw new IllegalStateException("결제 금액이 일치하지 않습니다.");
        }

        
        //3. 쿠폰 사용처리
        //쿠폰 타입, 쿠폰 유형,수수료
        Integer platformFee=0;
        String couponType="", discountType="";
        if (dto.getUserCouponId() != null) {            
            UserCoupon userCoupon = userCouponRepository.findById(dto.getUserCouponId())
                .orElseThrow(() -> new RuntimeException("쿠폰이 존재하지 않습니다."));
            userCoupon.useCoupon();
            userCouponRepository.save(userCoupon);
            
            
            if (userCoupon.getAdminCoupon() != null ) {
            	AdminCoupon adminCoupon = adminCouponRepository.findById(userCoupon.getAdminCoupon().getCouponId())
            			.orElseThrow(() -> new Exception("해당 어드민 쿠폰이 존재하지 않습니다."));
            	adminCoupon.incrementUsedCount();
            	adminCouponRepository.save(adminCoupon);
            	couponType="MG";
            	discountType=adminCoupon.getDiscountType();
            	platformFee = dto.getAmount()/10;
            }
            if (userCoupon.getClassCoupon() != null ) {
            	ClassCoupon classCoupon = classCouponRepository.findById(userCoupon.getClassCoupon().getClassCouponId())
            			.orElseThrow(() -> new Exception("해당 클래스 쿠폰이 존재하지 않습니다."));
            	classCoupon.incrementUsedCount();
            	classCouponRepository.save(classCoupon);
            	couponType="HT";
            	discountType=classCoupon.getDiscountType();
            	platformFee= payment.getClassPrice()/10;
            }
            userPaymentRepository.save(payment);
        } else {
        	platformFee=payment.getAmount()/10;
        }
        //최종 결제 저장
        payment.approve(regist, LocalDateTime.now(),couponType,discountType,platformFee); 
        //4. 수강생 수 업데이트
        cc.incrementRegisteredCount();
        classCalendarRepository.save(cc);
        //5. 강의 상태 업데이트
        if (cc.getHostClass().getRecruitMax().equals(cc.getRegisteredCount())) {
        	cc.changeStatus("모집마감");
            classCalendarRepository.save(cc);
        }       
        

	}


	@Transactional
	@Override
	public void initPayment(PaymentInitRequestDto dto, User user) throws Exception {
	    ClassCalendar calendar = classCalendarRepository.findById(dto.getCalendarId())
	            .orElseThrow(() -> new RuntimeException("일정을 찾을 수 없습니다."));
	    if (userPaymentRepository.existsByOrderNo(dto.getOrderNo())) {
	        throw new IllegalStateException("이미 존재하는 주문번호입니다.");
	    }

	    // 사전 결제 정보 저장 (status = "결제대기")
	    UserPayment payment = UserPayment.builder()
	            .orderNo(dto.getOrderNo())
	            .amount(dto.getAmount())
	            .paidAt(null) // 아직 결제 안 했으니 null
	            .paymentType(dto.getPaymentType())
	            .status("결제대기")
	            .classCalendar(calendar)
	            .classPrice(dto.getClassPrice())
	            .userCoupon(dto.getUserCouponId() != null
	                ? UserCoupon.builder().ucId(dto.getUserCouponId()).build()
	                : null)
	            .build();

	    userPaymentRepository.save(payment);		
	}	
}
