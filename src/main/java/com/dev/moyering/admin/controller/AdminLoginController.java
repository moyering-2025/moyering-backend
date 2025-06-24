package com.dev.moyering.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.moyering.user.service.UserService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AdminLoginController {

	@Autowired
	private UserService userService;
	
}