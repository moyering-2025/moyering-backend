package com.dev.moyering.gathering.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.querydsl.core.Tuple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dev.moyering.auth.PrincipalDetails;
import com.dev.moyering.gathering.dto.GatheringApplyDto;
import com.dev.moyering.gathering.dto.GatheringDto;
import com.dev.moyering.gathering.dto.GatheringInquiryDto;
import com.dev.moyering.gathering.service.GatheringApplyService;
import com.dev.moyering.gathering.service.GatheringInquiryService;
import com.dev.moyering.gathering.service.GatheringService;
import com.dev.moyering.user.dto.UserDto;
import com.dev.moyering.user.service.UserService;
import com.dev.moyering.util.PageInfo;
@RestController 
public class GatheringApplyController {
	@Autowired
	private GatheringService gatheringService;
	@Autowired
	private UserService userService;
	@Autowired
	private GatheringApplyService gatheringApplyService;
	@Autowired
	private GatheringInquiryService gatheringInquiryService;
	@PostMapping("/user/applyGathering")
	public ResponseEntity<GatheringApplyDto> applyToGathering(@AuthenticationPrincipal PrincipalDetails principal, 
			@RequestBody Map<String,Object> param) {
		
		return null;
//		try {
//			Integer gatheringInquiryId = gatheringInquiryService.writeGatheringInquiry(gatheringApplyDto);
//			if(gatheringInquiryId!=null) {
//				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//			}
//			return new ResponseEntity<>(HttpStatus.OK);
//		} catch(Exception e) {
//			e.printStackTrace();
//			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//		}
	}
	
	@PostMapping("/user/updateMemberApproval")
	public ResponseEntity<GatheringApplyDto> updateMemberApproval(@AuthenticationPrincipal PrincipalDetails principal,
			@ModelAttribute GatheringApplyDto gatheringApplyDto) {
		return null;
//		try {
//			Integer gatheringInquiryId = gatheringInquiryService.writeGatheringInquiry(gatheringApplyDto);
//			if(gatheringInquiryId!=null) {
//				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//			}
//			return new ResponseEntity<>(HttpStatus.OK);
//		} catch(Exception e) {
//			e.printStackTrace();
//			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//		}
	}
	@GetMapping("/getApplyListByGatheringId")
	public ResponseEntity<Map<String,Object>> getApplyListOfGathering( @RequestParam("gatheringId") Integer gatheringId) {
		 
		try {
			Map<String,Object> res = new HashMap();
			List<GatheringApplyDto> findApplyUserList = gatheringApplyService.findApplyUserListByGatheringId(gatheringId);
			if(findApplyUserList!=null) {
				return new ResponseEntity<>(null, HttpStatus.OK);
			} else {
				res.put("findApplyUserList", findApplyUserList);
			}
			return new ResponseEntity<>(res, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
}
