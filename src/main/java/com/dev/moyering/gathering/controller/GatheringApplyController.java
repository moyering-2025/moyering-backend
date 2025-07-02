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
	
	@PostMapping("/updateApproval")
	public ResponseEntity<Boolean> updateApproval(
				    @RequestParam(value = "applyId") int applyId,
				    @RequestParam(value = "isApprove") boolean isApprove){
		try {
			gatheringApplyService.updateGatheringApplyApproval(applyId, isApprove);
			return new ResponseEntity<Boolean>(true, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	@PostMapping("/user/cancelGatheringApply")
	public ResponseEntity<Boolean> cancelGatheringApply(
				    @RequestParam(value = "applyId") int applyId){
		try {
			gatheringApplyService.cancelGatheringApply(applyId);
			return new ResponseEntity<Boolean>(true, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	@PostMapping("/user/myApplyList")
	public ResponseEntity<Map<String, Object>> getMyApplyList(@AuthenticationPrincipal PrincipalDetails principal,
			@RequestBody(required = false) Map<String, String> param) {
		try {
			Integer userId = principal.getUser().getUserId();	
			System.out.println("로그인 유저 : "+ userId);
			System.out.println("param : "+param);
			String word = null;	
			PageInfo pageInfo = new PageInfo(1);
			String status = null;
			if(param != null) {
				if(param.get("page")!=null) {
					pageInfo.setCurPage(Integer.parseInt(param.get("page")));
				}
				word = param.get("word");
                status = param.get("status");
                if ("전체".equals(status)) {
                    status = null; // 전체인 경우 null로 처리
                }
			}
			Integer allCnt = 0, undefinedCnt = 0, approvedCnt = 0, canceledCnt = 0;
			allCnt = gatheringApplyService.selectMyApplyListCount(userId, word, "전체");
			undefinedCnt = gatheringApplyService.selectMyApplyListCount(userId, word, "대기중");
			approvedCnt = gatheringApplyService.selectMyApplyListCount(userId, word, "수락됨");
			canceledCnt = gatheringApplyService.selectMyApplyListCount(userId, word, "거절됨");
			
			List<GatheringApplyDto> applyList = null;
			
					Map<String, Object> response = new HashMap<>();
			if(allCnt > 0) {
				applyList = gatheringApplyService.findApplyListByApplyUserId(userId, pageInfo, word, status);
				response.put("list", applyList);				
				response.put("pageInfo", pageInfo);
			} else {
				response.put("list", "조회된 리스트 없음");
			}
			response.put("allCnt", allCnt);
			response.put("undefinedCnt", undefinedCnt);
			response.put("inProgressCnt", approvedCnt);
			response.put("canceledCnt", canceledCnt);
            
			return new ResponseEntity<>(response, HttpStatus.OK);
			
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

