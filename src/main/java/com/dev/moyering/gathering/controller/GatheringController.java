package com.dev.moyering.gathering.controller;

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
import com.dev.moyering.user.entity.User;
import com.dev.moyering.user.service.UserService;

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
	public ResponseEntity<GatheringDto> write(@AuthenticationPrincipal 
			PrincipalDetails principal, @ModelAttribute GatheringDto gatheringDto, 
			@RequestParam(name="thumbnail") MultipartFile thumbnail) {
		try {
			User user = principal.getUser();
			gatheringDto.setUserId(user.getUserId());
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
	@GetMapping("/getGatheringInquiries")
	public ResponseEntity<Map<String, Object>> inquiriesByGatheringId(@RequestParam("gatheringId") Integer gatheringId) {
		try {
			List<GatheringInquiryDto> gatheringInquiryList = gatheringInquiryService.gatheringInquiryListBygatheringId(gatheringId);
			//호스트,신청 멤버 정보 추가
			Map<String,Object> res = new HashMap<>();
			res.put("gathering", gatheringInquiryList);
//			UserDto userDto = userService.findUserByUserId(nGatheringDto.getUserId());
			List<GatheringApplyDto> member = gatheringApplyService.findApplyUserListByGatheringId(gatheringId);
//			res.put("host", userDto);
			res.put("member", member);
			return new ResponseEntity<>(res, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		/*
		 * 	try {
			String type = null;
			String word = null;
			PageInfo pageInfo = new PageInfo(1);
			
			if(param!=null) {
				if(param.get("page")!=null) {
					pageInfo.setCurPage(Integer.parseInt(param.get("page")));
				}
				type = param.get("type");
				word = param.get("word");
				System.out.println("type : "+type+" word : "+word );
			}
			List<ArticleDto> articleList = boardService.searchArticleList(pageInfo, type, word);
			System.out.println(articleList + "작성자 검색");
			Map<String, Object> res = new HashMap<>();
			res.put("boardList", articleList);
			res.put("pageInfo", pageInfo);
			return new ResponseEntity<>(res, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}	
	}*/
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
	@PostMapping("/user/responseToGatheringInquiry")
	public ResponseEntity<GatheringInquiryDto> responseToGatheringInquiry(@ModelAttribute GatheringInquiryDto gatheringInquiryDto) {
		return null;
//		try {
//			Integer gatheringInquiryId = gatheringInquiryService.writeGatheringInquiry(gatheringInquiryDto);
//			if(gatheringInquiryId!=null) {
//				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//			}
//			return new ResponseEntity<>(HttpStatus.OK);
//		} catch(Exception e) {
//			e.printStackTrace();
//			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//		}
	}
	@PostMapping("/user/applyGathering")
	public ResponseEntity<GatheringApplyDto> applyToGathering(@ModelAttribute GatheringApplyDto gatheringApplyDto) {
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
	public ResponseEntity<GatheringApplyDto> updateMemberApproval(@ModelAttribute GatheringApplyDto gatheringApplyDto) {
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
}
