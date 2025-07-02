package com.dev.moyering.common.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dev.moyering.common.service.EmailService;
import com.dev.moyering.user.dto.UserDto;
import com.dev.moyering.user.service.UserService;

@RestController
public class EmailController {

	@Autowired
	private EmailService emailService;
	@Autowired
	private UserService userService;
	
	@PostMapping("/api/auth/register")
	public String register(@RequestBody UserDto userDto) {
		try {
			userService.join(userDto);
			return "이메일 인증을 확인하세요!";
		}catch (Exception e) {
			e.printStackTrace();
			return "회원가입 실패"+e.getMessage();
		}
	}
	
	@GetMapping("/api/auth/verify")
	public String verifyEmail(@RequestParam String token) {
		try{
			Boolean isVerified = userService.verifyEmail(token);
			if(isVerified) {
				return "이메일 인증 완료!";
			}
			return "인증 실패. 다시 시도해주세요.";			
		}catch (Exception e) {
			return "이메일 인증 실패"+e.getMessage();
		}
	}
}
