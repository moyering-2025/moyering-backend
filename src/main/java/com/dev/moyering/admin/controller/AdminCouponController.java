package com.dev.moyering.admin.controller;

import com.dev.moyering.admin.dto.AdminCouponDto;
import com.dev.moyering.admin.dto.AdminCouponSearchCond;
import com.dev.moyering.admin.dto.AdminNoticeDto;
import com.dev.moyering.admin.service.AdminCouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/coupon")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class AdminCouponController {
    private final AdminCouponService adminCouponService;

    /*** 쿠폰 리스트 조회*/
    @GetMapping
    public ResponseEntity<Page<AdminCouponDto>> getCouponList(
            @ModelAttribute AdminCouponSearchCond cond, // 쿼리 파라미터나 폼데이터에서 데이터 가져오기
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        try {
            Page<AdminCouponDto> result = adminCouponService.getCouponList(cond, pageable);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("쿠폰 목록 조회 중 오류 발생", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /*** 쿠폰 생성*/
    @PostMapping
    public ResponseEntity<AdminCouponDto> createCoupon(@RequestBody AdminCouponDto couponDto) {
        log.info("쿠폰 등록 요청: {}",  couponDto.getCouponId());

        try {
            AdminCouponDto createdCoupon = adminCouponService.createCoupon(couponDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCoupon);
        } catch (Exception e) {
            log.error("쿠폰 등록 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /*** 쿠폰 수정*/
    @PutMapping("/{couponId}")
    public ResponseEntity<AdminCouponDto> updateCoupon(@PathVariable Integer couponId, @RequestBody AdminCouponDto couponDto){
        log.info("쿠폰 생성 요청 : {}",  couponId);

        try {
            AdminCouponDto updateCoupon = adminCouponService.updateCoupon(couponId, couponDto);
            return ResponseEntity.ok(updateCoupon);
        } catch (Exception e){
            log.error("쿠폰 수정 요청 실패 : {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /*** 쿠폰 삭제*/
    @DeleteMapping("/{couponId}")
    public ResponseEntity<AdminCouponDto> deleteCoupon(@PathVariable Integer couponId, @RequestBody AdminCouponDto couponDto) {
        log.info("쿠폰 삭제 요청 : {}", couponId);

        try {
            AdminCouponDto deleteCoupon = adminCouponService.deleteCoupon(couponId);
            return ResponseEntity.ok(deleteCoupon);
        } catch (Exception e){
            log.error("쿠폰 삭제 실패 요청 : {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
