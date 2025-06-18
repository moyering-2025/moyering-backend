package com.dev.moyering.gathering.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.querydsl.core.Tuple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dev.moyering.common.dto.UserDto;
import com.dev.moyering.common.service.UserService;
import com.dev.moyering.gathering.dto.GatheringApplyDto;
import com.dev.moyering.gathering.dto.GatheringDto;
import com.dev.moyering.gathering.dto.GatheringInquiryDto;
import com.dev.moyering.gathering.service.GatheringApplyService;
import com.dev.moyering.gathering.service.GatheringInquiryService;
import com.dev.moyering.gathering.service.GatheringService;

@RestController 
public class GatheringController {
	@Autowired
	private GatheringService gatheringService;
	@Autowired
	private UserService userService;
	@Autowired
	private GatheringApplyService gatheringApplyService;
	@Autowired
	private GatheringInquiryService gatheringInquiryService;
	
	@PostMapping("/user/writeGathering")
	public ResponseEntity<GatheringDto> write(@ModelAttribute GatheringDto gatheringDto, 
			@RequestParam(name="thumbnail") MultipartFile thumbnail) {
		try {
			System.out.println("gatheringDto : "+gatheringDto +", "+thumbnail);
			Integer gatheringId = gatheringService.writeGathering(gatheringDto, thumbnail);
			System.out.println("게더링 등록 성공 새 게더링 아이디 : " + gatheringId);
			GatheringDto nGatheringDto = gatheringService.detailGathering(gatheringId);
			return new ResponseEntity<>(nGatheringDto, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	@PostMapping("/user/modifyGathering")
	public ResponseEntity<GatheringDto> modify(@ModelAttribute GatheringDto gatheringDto, 
			@RequestParam(name="thumbnail") MultipartFile thumbnail) {
		try {
			System.out.println("gatheringDto : "+gatheringDto +", "+thumbnail);
			gatheringService.modifyGathering(gatheringDto, thumbnail);
			GatheringDto nGatheringDto = gatheringService.detailGathering(gatheringDto.getGatheringId());
			return new ResponseEntity<>(nGatheringDto, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	@GetMapping("/detailGathering")
	public ResponseEntity<Map<String,Object>> detail(@RequestParam("gatheringId") Integer gatheringId) {
		try {
			GatheringDto nGatheringDto = gatheringService.detailGathering(gatheringId);
			//호스트,신청 멤버 정보 추가
			Map<String,Object> res = new HashMap<>();
			res.put("gathering", nGatheringDto);
			UserDto userDto = userService.findUserByUserId(nGatheringDto.getUserId());
			List<GatheringApplyDto> member = gatheringApplyService.findApplyUserListByGatheringId(gatheringId);
			res.put("host", userDto);
			res.put("member", member);
			return new ResponseEntity<>(res, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}	
	@PostMapping("/user/writeGatheringInquiry")
	public ResponseEntity<GatheringInquiryDto> writeGatheringInquiry(@ModelAttribute GatheringInquiryDto gatheringInquiryDto) {
		try {
			Integer gatheringInquiryId = gatheringInquiryService.writeGatheringInquiry(gatheringInquiryDto);
			if(gatheringInquiryId!=null) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			return new ResponseEntity<>(HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
}
