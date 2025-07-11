package com.dev.moyering.admin.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.moyering.admin.service.AdminDashBoardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AdminDashBoardController {
	private final AdminDashBoardService dashBoardService;
	
	
	@GetMapping("/api/dashBoard")
	public ResponseEntity<Map<String,Object>> dashBoard() {
		try {
			Map<String,Object> map = dashBoardService.AdminDashBoard();
			return new ResponseEntity<>(map,HttpStatus.OK);
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
}
