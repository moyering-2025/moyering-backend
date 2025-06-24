package com.dev.moyering.gathering.controller;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dev.moyering.auth.PrincipalDetails;
import com.dev.moyering.gathering.dto.GatheringApplyDto;
import com.dev.moyering.gathering.dto.GatheringDto;
import com.dev.moyering.gathering.dto.GatheringInquiryDto;
import com.dev.moyering.gathering.service.GatheringApplyService;
import com.dev.moyering.gathering.service.GatheringInquiryService;
import com.dev.moyering.gathering.service.GatheringLikesService;
import com.dev.moyering.gathering.service.GatheringService;
import com.dev.moyering.user.dto.UserDto;
import com.dev.moyering.user.service.UserService;
import com.dev.moyering.util.PageInfo;

@RestController 
public class GatheringController {
	@Autowired
	private GatheringService gatheringService;
	@Autowired
	private UserService userService;
	@Autowired
	private GatheringApplyService gatheringApplyService;
	@Autowired
	private GatheringLikesService gatheringLikesService;

	@PostMapping("/user/writeGathering")
	public ResponseEntity<Integer> write(@AuthenticationPrincipal PrincipalDetails principal, 
			@ModelAttribute GatheringDto gatheringDto, 
			@RequestParam(name="thumbnail") MultipartFile thumbnail) {
		try {
			gatheringDto.setUserId(principal.getUser().getUserId());
			gatheringDto.setThumbnailFileName(thumbnail.getOriginalFilename());
			System.out.println("gatheringDto : "+gatheringDto +", "+thumbnail);
			Integer gatheringId = gatheringService.writeGathering(gatheringDto, thumbnail);
			System.out.println("게더링 등록 성공 새 게더링 아이디 : " + gatheringId);
			return new ResponseEntity<>(gatheringId, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	@PostMapping("/user/modifyGathering")
	public ResponseEntity<GatheringDto> modify(@AuthenticationPrincipal PrincipalDetails principal, 
			@ModelAttribute GatheringDto gatheringDto, 
			@RequestParam(name="thumbnail", required = false) MultipartFile thumbnail) {
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
			System.out.println("조회된 게더링 : " + nGatheringDto);
			Map<String,Object> res = new HashMap<>();
			res.put("gathering", nGatheringDto);
			UserDto userDto = userService.findUserByUserId(nGatheringDto.getUserId());
			List<GatheringApplyDto> member = gatheringApplyService.findApplyUserListByGatheringId(gatheringId);
//			Integer totalLikeNum = gatheringLikesService.getTotalLikesOfGatheringByGatheringId(gatheringId);
			userDto.setPassword(null);
			res.put("organizer", userDto);
			res.put("member", member);
//			res.put("totalLikeNum", totalLikeNum);
			return new ResponseEntity<>(res, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}	
	
	@GetMapping("/user/detailGathering")
	public ResponseEntity<Map<String,Object>> extraDetail(@AuthenticationPrincipal PrincipalDetails principal, 
			@RequestParam("gatheringId") Integer gatheringId) {
		try {
			//찜 여부, 신청여부
			Map<String,Object> res = new HashMap<>();
			System.out.println("로그인 정보 : "+principal.getUser().getUserId());
			Integer cntApply = gatheringApplyService.findByGatheringIdAndUserId(gatheringId, principal.getUser().getUserId());
			System.out.println("cnt :" + cntApply);
			if(cntApply !=null && cntApply != 0) {
				res.put("canApply", false);
			} else {
				res.put("canApply", true);
			}
			
			Boolean isLiked = gatheringLikesService.getGatheringLike(principal.getUser().getUserId(), gatheringId);
			
			res.put("isLiked", isLiked);
			
			return new ResponseEntity<>(res, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}	
	@PostMapping("/user/toggleGatheringLike")
	@ResponseBody
	public String toggleGatheringLike(@AuthenticationPrincipal PrincipalDetails principal, 
			@RequestParam("gatheringId") Integer gatheringId) {
		try {
			System.out.println("로그인된 아이디 : "+principal.getUser().getUserId());
			Boolean isLike = gatheringLikesService.toggleGatheringLike(principal.getUser().getUserId(), gatheringId);
			return String.valueOf(isLike);
		} catch(Exception e) {
			e.printStackTrace();
			return String.valueOf(false);
		}
	}
	@GetMapping("/user/detailForModifyGathering")
	public ResponseEntity<GatheringDto> detailForModifyGathering(@AuthenticationPrincipal PrincipalDetails principal, 
			@RequestParam("gatheringId") Integer gatheringId) {
		try {
	        System.out.println("API 호출됨!");
	        System.out.println("gatheringId: " + gatheringId);
	        System.out.println("로그인 정보 : "+principal.getUser().getUserId());
			GatheringDto gatheringDto = gatheringService.detailGathering(gatheringId);
			System.out.println("조회된 게더링 : " + gatheringDto);
			if(gatheringDto==null) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			} else {
				if(gatheringDto.getUserId()==principal.getUser().getUserId()) {
					return new ResponseEntity<>(gatheringDto, HttpStatus.OK);
				} else {
					return new ResponseEntity<>(HttpStatus.FORBIDDEN); 
				}
				
			}
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/user/myGatheringList")
	public ResponseEntity<List<GatheringDto>> myGatheringList(@AuthenticationPrincipal PrincipalDetails principal, 
//			@RequestParam Integer userId, 
			@RequestBody(required=false) Map<String,String> param){
		try {
			Integer userId = principal.getUser().getUserId();
			String word = null;	
			PageInfo pageInfo = new PageInfo(1);
			if(param != null) {
				if(param.get("page")!=null) {
					pageInfo.setCurPage(Integer.parseInt(param.get("page")));
				}
				word = param.get("word");
			}
			List<GatheringDto> myGatheringList = gatheringService.myGatheringList(userId, pageInfo, word);
			
			return new ResponseEntity<>(myGatheringList, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
}
