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
	public ResponseEntity<Integer> write(@AuthenticationPrincipal 
			PrincipalDetails principal, @ModelAttribute GatheringDto gatheringDto, 
			@RequestParam(name="thumbnail") MultipartFile thumbnail) {
		try {
			System.out.println(gatheringDto);
			//User user = principal.getUser();
			//gatheringDto.setUserId(user.getUserId());
			gatheringDto.setThumbnailFileName(thumbnail.getOriginalFilename());
			System.out.println("gatheringDto : "+gatheringDto +", "+thumbnail);
			Integer gatheringId = gatheringService.writeGathering(gatheringDto, thumbnail);
			System.out.println("게더링 등록 성공 새 게더링 아이디 : " + gatheringId);
//			Integer nGatheringId = gatheringService.detailGathering(gatheringId);
			return new ResponseEntity<>(gatheringId, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	@PostMapping("/user/modifyGathering")
	public ResponseEntity<GatheringDto> modify(@ModelAttribute GatheringDto gatheringDto, 
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
			userDto.setPassword(null);
			res.put("host", userDto);
			res.put("member", member);
			return new ResponseEntity<>(res, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}	
	@GetMapping("/detailForModifyGathering")
	public ResponseEntity<GatheringDto> detailForModifyGathering(@RequestParam("gatheringId") Integer gatheringId) {
		try {
			GatheringDto gatheringDto = gatheringService.detailGathering(gatheringId);
			//호스트,신청 멤버 정보 추가
			System.out.println("조회된 게더링 : " + gatheringDto);
			return new ResponseEntity<>(gatheringDto, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}	
	@GetMapping("/getGatheringInquiries")
	public ResponseEntity<Map<String, Object>> inquiriesByGatheringId(@RequestParam("gatheringId") Integer gatheringId) {
		try {
			Map<String,Object> res = new HashMap<>();
			List<GatheringInquiryDto> gatheringInquiryList = gatheringInquiryService.gatheringInquiryListBygatheringId(gatheringId);
			//호스트,신청 멤버 정보 추가
			GatheringDto nGatheringDto = gatheringService.detailGathering(gatheringId);
			res.put("gathering", gatheringInquiryList);
			res.put("nGatheringDto", nGatheringDto);
			return new ResponseEntity<>(res, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}	
	@GetMapping("/getGatheringInquiriesByUserId")
	public ResponseEntity<Map<String, Object>> inquiriesByUserId(@RequestParam("gatheringId") Integer gatheringId) {
		//마이페이지를 위한 문의내역 불러오기
		try {
			List<GatheringInquiryDto> gatheringInquiryList = gatheringInquiryService.gatheringInquiryListBygatheringId(gatheringId);
			Map<String,Object> res = new HashMap<>();
			GatheringDto nGatheringDto = gatheringService.detailGathering(gatheringId);
			res.put("gathering", gatheringInquiryList);
			res.put("nGatheringDto", nGatheringDto);
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
