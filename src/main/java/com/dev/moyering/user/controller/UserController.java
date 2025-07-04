package com.dev.moyering.user.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dev.moyering.user.dto.UserBadgeDto;
import com.dev.moyering.user.dto.UserProfileDto;
import com.dev.moyering.user.dto.UserProfileUpdateDto;
import com.dev.moyering.user.entity.UserBadge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.dev.moyering.auth.PrincipalDetails;
import com.dev.moyering.common.dto.SubCategoryDto;
import com.dev.moyering.common.service.SubCategoryService;
import com.dev.moyering.host.service.HostService;
import com.dev.moyering.user.dto.UserDto;
import com.dev.moyering.user.entity.User;
import com.dev.moyering.user.service.UserService;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private HostService hostService;
	@Autowired
	private SubCategoryService subCategoryService;
	
	@PostMapping("/join")
	public ResponseEntity<Object> join(@ModelAttribute User user){
		System.out.println("user"+user);
		UserDto userDto = user.toDto();
		try {
			userService.completeJoin(userDto);			
			return new ResponseEntity<>(HttpStatus.OK);
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/categories")
	public ResponseEntity<List<SubCategoryDto>> categories(){
		try{
			List<SubCategoryDto> subList = subCategoryService.getSubCategoriesWithParent();			
			return new ResponseEntity<>(subList,HttpStatus.OK);
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			
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

	@PatchMapping("/user/mypage/profile")
	public ResponseEntity<UserProfileUpdateDto> updateProfile(
			@AuthenticationPrincipal PrincipalDetails principal,
			@RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
			@RequestPart("profileData") UserProfileUpdateDto dto
	) throws Exception {
		Integer userId = principal.getUser().getUserId();
		userService.updateUserProfile(userId, dto, profileImage);
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}

	@GetMapping("/user/mypage/profile")
	public ResponseEntity<UserProfileDto> getMyProfile(@AuthenticationPrincipal PrincipalDetails principal) {
		Integer userId = principal.getUser().getUserId();
        UserProfileDto dto = null;
        try {
            dto = userService.getMyProfile(userId);
			return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
	}

	// 내가 가진 배지 리스트
	@GetMapping("/user/badges")
	public ResponseEntity<List<UserBadgeDto>> getMyBadges(
			@AuthenticationPrincipal PrincipalDetails principal
	) {
		Integer userId = principal.getUser().getUserId();
		List<UserBadgeDto> badges = userService.getUserBadges(userId);
		return ResponseEntity.ok(badges);
	}

	// 대표 배지 변경
	@PatchMapping("/user/mypage/badge")
	public ResponseEntity<?> updateMyBadge(
			@AuthenticationPrincipal PrincipalDetails principal,
			@RequestBody UserBadgeDto dto
	) {
		Integer userId = principal.getUser().getUserId();
        try {
            userService.updateRepresentativeBadge(userId, dto.getUserBadgeId());
			return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

	}

	@GetMapping("/user/firstBadge")
	public ResponseEntity<UserBadgeDto> getFirstBadge(
			@AuthenticationPrincipal PrincipalDetails principal
	){
		System.out.println("아이디"+principal.getUser().getUserId());
		UserBadge badge = userService.getUserFirstBadge(principal.getUser().getUserId());

		return new ResponseEntity<>(badge.toDto(),HttpStatus.OK);
	}
}
