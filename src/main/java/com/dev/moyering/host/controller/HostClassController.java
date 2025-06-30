package com.dev.moyering.host.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dev.moyering.admin.dto.AdminCouponDto;
import com.dev.moyering.admin.service.AdminCouponService;
import com.dev.moyering.host.dto.HostClassDto;
import com.dev.moyering.host.dto.InquiryDto;
import com.dev.moyering.host.service.HostClassService;
import com.dev.moyering.host.service.InquiryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class HostClassController {
    private final HostClassService hostClassService;
    private final InquiryService inquiryService;
    private final AdminCouponService adminCouponService;

   
    
    @GetMapping("/host/calendar")
    public ResponseEntity<List<HostClassDto>> selectClassCalendar(@RequestParam Integer hostId){
    	try {
    		List<HostClassDto> hostClasses = hostClassService.selectHostClassByHostId(hostId);
    		return new ResponseEntity<>(hostClasses,HttpStatus.OK);
    	}catch (Exception e) {
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
    }
    
    @GetMapping("/host/inquiry")
    public ResponseEntity<List<InquiryDto>> selectInquiry(@RequestParam Integer hostId){
    	try{
    		List<InquiryDto> inquiryDtoList = inquiryService.selectInquiryByHostId(hostId);
    		return ResponseEntity.ok(inquiryDtoList);
    	}catch (Exception e) {
    		e.printStackTrace();
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
    }
    
    @PostMapping("/host/inquiryReply")
    public ResponseEntity<Object> inquiryReply(@RequestParam Integer inquiryId,@RequestParam Integer hostId,@RequestParam String iqResContent){
    	try {
    		inquiryService.replyInquiry(inquiryId, hostId, iqResContent);
    		return new ResponseEntity<>(HttpStatus.OK);
    	}catch (Exception e) {
    		e.printStackTrace();
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}
    }
    
    @GetMapping("/host/couponList")
    public ResponseEntity<List<AdminCouponDto>> hostCouponList(){
    	try {
    		List<AdminCouponDto> couponList = adminCouponService.selectHostAllCoupon("HT");
    		return new ResponseEntity<>(couponList,HttpStatus.OK);
    	}catch (Exception e) {
    		e.printStackTrace();
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}
    }
    
   
    
}
