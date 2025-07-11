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
import com.dev.moyering.gathering.dto.MessageDto;
import com.dev.moyering.gathering.service.GatheringApplyService;
import com.dev.moyering.gathering.service.GatheringInquiryService;
import com.dev.moyering.gathering.service.GatheringService;
import com.dev.moyering.gathering.service.MessageService;
import com.dev.moyering.user.dto.UserDto;
import com.dev.moyering.user.service.UserService;
import com.dev.moyering.util.PageInfo;
@RestController 
public class MessageController {
	@Autowired
	private GatheringService gatheringService;
	@Autowired
	private UserService userService;
	@Autowired
	private GatheringApplyService gatheringApplyService;
	@Autowired
	private MessageService messageService;
	
	@PostMapping("/user/sendMessage")
	@ResponseBody
	public ResponseEntity<Boolean> sendMessage(@AuthenticationPrincipal PrincipalDetails principal, 
			@RequestBody Map<String, Object> param){
		try {
			Boolean sendSuccess = null;
			Integer gatheringId = (Integer) param.get("gatheringId");
			String messageContent = (String) param.get("content");
			sendSuccess = messageService.sendMessage(gatheringId, principal.getUser().getUserId(), messageContent);
			return new ResponseEntity<Boolean>(sendSuccess, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	@GetMapping("/user/messageRoomList")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> messageRoomList(@AuthenticationPrincipal PrincipalDetails principal){
		try {
			List<MessageDto> availableMessageRoomList = messageService.getAvailableMessageRoomList(principal.getUser().getUserId());
			List<MessageDto> disableMessageRoomList = messageService.getDisableMessageRoomList(principal.getUser().getUserId());
	        Map<String, Object> res = new HashMap<>();
	        res.put("availableMessageRoomList", availableMessageRoomList);
	        res.put("disableMessageRoomList", disableMessageRoomList);
	        return new ResponseEntity<>(res, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	@GetMapping("/user/messageRoom/{roomId}")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> messageRoomByGatheringId(@AuthenticationPrincipal PrincipalDetails principal,
			@PathVariable(value = "roomId") int roomId){
		try {
			System.out.println("로그인된 아이디 : "+principal.getUser().getUserId());
			List<MessageDto> getMessageRoomByGatheringId = messageService.getMessageListByGatheringIdAndUserId(roomId, principal.getUser().getUserId());
	        Map<String, Object> res = new HashMap<>();
	        res.put("myMessageRoomList", getMessageRoomByGatheringId);
	        return new ResponseEntity<>(res, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
}

