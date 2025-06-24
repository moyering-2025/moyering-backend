package com.dev.moyering.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dev.moyering.user.dto.UserDto;
import com.dev.moyering.user.entity.User;
import com.dev.moyering.user.service.UserService;

@RestController
public class UserController {

	@Autowired
	private UserService userService;
	
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

}
