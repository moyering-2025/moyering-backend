package com.dev.moyering.host.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.dev.moyering.host.dto.ClassCouponDto;
import com.dev.moyering.host.entity.ClassCoupon;
import com.dev.moyering.host.repository.ClassCouponRepository;
import com.dev.moyering.user.entity.User;
import com.dev.moyering.user.entity.UserCoupon;
import com.dev.moyering.user.repository.UserCouponRepository;
import com.dev.moyering.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClassCouponServiceImpl implements ClassCouponService {
	private final ClassCouponRepository classCouponRepository;
	private final UserRepository userRepository;
	private final UserCouponRepository userCouponRepository;
	
	@Override
	public List<ClassCouponDto> getCouponByClassId(Integer classId) throws Exception  {
		return classCouponRepository.findAllByClassId(classId).stream()
				.map(c->c.toDto()).collect(Collectors.toList());
	}
	
	@Transactional
	@Override
	public void downloadClassCoupon(Integer userId, Integer classCouponId) throws Exception {
	    User user = userRepository.findById(userId)
	            .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다"));
	    System.out.println("ddddddddddsfliejlnldshf/olidhdf");
        // 클래스 쿠폰 조회
        ClassCoupon classCoupon = classCouponRepository.findById(classCouponId)
            .orElseThrow(() -> new RuntimeException("쿠폰을 찾을 수 없습니다"));
        // 이미 다운로드한 경우 예외 처리 (선택)
        boolean alreadyDownloaded = userCouponRepository.existsByUserAndClassCoupon(user, classCoupon);
        if (alreadyDownloaded) {
            throw new RuntimeException("이미 다운로드한 쿠폰입니다");
        }
        
        // 사용 가능 매수 확인
        if (classCoupon.getUsedCnt() >= classCoupon.getAmount()) {
            throw new RuntimeException("쿠폰이 모두 소진되었습니다");
        }
        
        //유저 쿠폰 테이블에 저장
        UserCoupon userCoupon = UserCoupon.builder()
        		.user(user)
        		.classCoupon(classCoupon)
        		.downloadedAt(LocalDateTime.now())
        		.status("미사용")
        		.build();
        userCouponRepository.save(userCoupon);
        
        //클래스 쿠폰의 사용량 증가
        classCoupon.setUsedCnt(classCoupon.getUsedCnt()+1);
        classCouponRepository.save(classCoupon);
	}

}
