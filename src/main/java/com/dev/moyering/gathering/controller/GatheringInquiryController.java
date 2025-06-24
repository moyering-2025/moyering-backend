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
import org.springframework.web.bind.annotation.RequestAttribute;
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
public class GatheringInquiryController {
	@Autowired
	private GatheringService gatheringService;
	@Autowired
	private UserService userService;
	@Autowired
	private GatheringApplyService gatheringApplyService;
	@Autowired
	private GatheringInquiryService gatheringInquiryService;
	@GetMapping("/getGatheringInquiries")
	public ResponseEntity<Map<String, Object>> inquiriesByGatheringId(@RequestParam("gatheringId") Integer gatheringId) {
	    try {
	        Map<String, Object> res = new HashMap<>();
	        List<GatheringInquiryDto> gatheringInquiryList = gatheringInquiryService.gatheringInquiryListBygatheringId(gatheringId);
	        
	        // 호스트, 신청 멤버 정보 추가
	        GatheringDto nGatheringDto = gatheringService.detailGathering(gatheringId);
	        
	        // Q&A 리스트를 프론트엔드 구조에 맞게 변환
	        List<Map<String, Object>> transformedQnaList = new ArrayList<>();
	        
	        for (GatheringInquiryDto inquiry : gatheringInquiryList) {
	            Map<String, Object> qnaItem = new HashMap<>();
	            
	            qnaItem.put("id", inquiry.getInquiryId());
	            qnaItem.put("status", inquiry.getResponseState()); // "답변대기" 또는 "답변완료"
	            qnaItem.put("content", inquiry.getInquiryContent());
	            qnaItem.put("author", inquiry.getNickName());
	            qnaItem.put("date", inquiry.getInquiryDate());
	            
	            // 답변이 있는 경우 answer 객체 생성
	            if (inquiry.getResponseContent() != null && !inquiry.getResponseContent().trim().isEmpty()) {
	                Map<String, Object> answer = new HashMap<>();
	                answer.put("author", nGatheringDto.getNickName());
	                answer.put("date", inquiry.getResponseDate());
	                // 답변 내용을 배열로 변환 (줄바꿈 기준으로 분리)
	                List<String> contentLines = Arrays.asList(inquiry.getResponseContent().split("\n"));
	                answer.put("content", contentLines);
	                
	                qnaItem.put("answer", answer);
	            } else {
	                qnaItem.put("answer", null);
	            }
	            
	            transformedQnaList.add(qnaItem);
	        }
	        
	        // 응답 데이터 구성
	        res.put("qnaList", transformedQnaList);
	        res.put("gatheringTitle", nGatheringDto.getTitle());
	        res.put("gatheringThumbnailFileName", nGatheringDto.getThumbnailFileName());
	        res.put("gatheringMeetingDate", nGatheringDto.getMeetingDate());
	        res.put("gatheringStartTime", nGatheringDto.getStartTime());
	        res.put("gatheringEndTime", nGatheringDto.getEndTime());
	        res.put("gatheringAddress", nGatheringDto.getAddress());
	        res.put("gatheringDetailAddress", nGatheringDto.getDetailAddress());
	        res.put("gatheringTags", nGatheringDto.getTags());
	        res.put("gatheringIntrOnln", nGatheringDto.getIntrOnln());
	        
	        return new ResponseEntity<>(res, HttpStatus.OK);
	    } catch(Exception e) {
	        e.printStackTrace();
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	    }
	}
	@GetMapping("/user/getGatheringInquiriesByUserId")
	public ResponseEntity<Map<String, Object>> inquiriesByUserId(@AuthenticationPrincipal PrincipalDetails principal, 
			@RequestParam("gatheringId") Integer gatheringId) {
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
	public ResponseEntity<Integer> writeGatheringInquiry(@AuthenticationPrincipal PrincipalDetails principal, 
		@RequestBody  Map<String, Object> requestData) {
		try {
			 GatheringInquiryDto gatheringInquiryDto = new GatheringInquiryDto();
			gatheringInquiryDto.setGatheringId(Integer.valueOf(requestData.get("gatheringId").toString()));
			gatheringInquiryDto.setInquiryContent(requestData.get("inquiryContent").toString());
			gatheringInquiryDto.setUserId(principal.getUser().getUserId());
			System.out.println("여기까지는 옴 : "+ gatheringInquiryDto);
			Integer gatheringInquiryId = gatheringInquiryService.writeGatheringInquiry(gatheringInquiryDto);
			if(gatheringInquiryId!=null) {
				return new ResponseEntity<>(gatheringInquiryId, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);				
			}
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/user/responseToGatheringInquiry")
	public ResponseEntity<GatheringInquiryDto> responseToGatheringInquiry(@AuthenticationPrincipal PrincipalDetails principal, 
			@ModelAttribute GatheringInquiryDto gatheringInquiryDto) {
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
}
