package com.dev.moyering.user.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


import com.dev.moyering.auth.PrincipalDetails;
import com.dev.moyering.host.entity.Host;
import com.dev.moyering.host.service.HostService;
import com.dev.moyering.user.dto.UserDto;
import com.dev.moyering.user.entity.User;
import com.dev.moyering.user.service.UserService;

@RestController
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private HostService hostService;
	
	@PostMapping("/join")
	public ResponseEntity<Object> join(@ModelAttribute User user){
		System.out.println("user"+user);
		UserDto userDto = user.toDto();
		try {
			userService.join(userDto);			
			return new ResponseEntity(HttpStatus.OK);
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
	}




	//feed
	@GetMapping("/socialing/feedUser/{nickname}")
	public ResponseEntity<UserDto> getUserByNickname(@PathVariable String nickname) {
		try {
			UserDto user = userService.getByNickname(nickname);
			return ResponseEntity.ok(user);
		} catch (Exception e) {
			// 닉네임이 없으면 404
			return ResponseEntity.notFound().build();
		}
	}

	
	@PostMapping("/userInfo")
	public ResponseEntity<Map<String,Object>> userInfo(@AuthenticationPrincipal PrincipalDetails principalDetails){
		try {
			User user = principalDetails.getUser();
			Map<String, Object> userInfo = new HashMap<>();
			userInfo.put("id", user.getUserId());
			userInfo.put("username", user.getUsername());
			userInfo.put("name", user.getName());
			userInfo.put("email", user.getEmail());
			userInfo.put("userType", user.getUserType());
			System.out.println(user.getNickName());
			userInfo.put("nickName", user.getNickName());
			userInfo.put("profile", user.getProfile());
			if (user.getUserType().equals("ROLE_HT")) {
				Integer hostId = hostService.findByUserId(user.getUserId()).getHostId();
				userInfo.put("hostId", hostId);
			}
			return new ResponseEntity<>(userInfo, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
}
