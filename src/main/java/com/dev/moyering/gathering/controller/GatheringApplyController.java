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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
	
	@PostMapping("/user/applyGathering")
	@ResponseBody
	public String applyGathering(@AuthenticationPrincipal PrincipalDetails principal, 
			@RequestBody Map<String, Object> param){
		try {
			System.out.println("로그인된 아이디 : "+principal.getUser().getUserId());
			Integer gatheringId = (Integer) param.get("gatheringId");
			String aspiration = (String) param.get("aspiration");
			GatheringApplyDto gatheringApplyDto = new GatheringApplyDto();
			gatheringApplyDto.setGatheringId(gatheringId);
			gatheringApplyDto.setUserId(principal.getUser().getUserId());
			gatheringApplyDto.setAspiration(aspiration);
			Integer res = gatheringApplyService.applyToGathering(gatheringApplyDto);
			return String.valueOf(res!=null);
		} catch(Exception e) {
			e.printStackTrace();
			return String.valueOf(false);
		}
	}
	
	@PostMapping("/user/updateMemberApproval")
	public ResponseEntity<Boolean> updateMemberApproval(@AuthenticationPrincipal PrincipalDetails principal,
			@ModelAttribute GatheringApplyDto gatheringApplyDto) {
		try {
			gatheringApplyService.updateGatheringApplyApproval(gatheringApplyDto.getGatheringApplyId(), gatheringApplyDto.getIsApprove());
			return new ResponseEntity<Boolean>(true, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	@GetMapping("/user/myApplyList")
	public ResponseEntity<List<GatheringApplyDto>> getMyApplyList(@AuthenticationPrincipal PrincipalDetails principal,
			@ModelAttribute GatheringApplyDto gatheringApplyDto) {
		try {
			List<GatheringApplyDto> applyList = null;
			applyList = gatheringApplyService.findApplyListByApplyUserId(principal.getUser().getUserId());
			return new ResponseEntity<>(applyList, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/getApplyListByGatheringId/{gatheringId}")
	public ResponseEntity<List<GatheringApplyDto>> getApplyListOfGatheringForOrganizer(@PathVariable("gatheringId") Integer gatheringId) {
		
		try {
			List<GatheringApplyDto> findApplyUserList = null;
			findApplyUserList = gatheringApplyService.findApplyUserListByGatheringIdForOrganizer(gatheringId);
			return new ResponseEntity<>(findApplyUserList, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
}

