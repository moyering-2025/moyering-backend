package com.dev.moyering.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.moyering.common.dto.UserDto;
import com.dev.moyering.common.entity.User;
import com.dev.moyering.common.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;

	public UserDto findUserByUserId(Integer userId) throws Exception{
		User user = userRepository.findById(userId).orElseThrow(()->new Exception("멤버 조회 오류"));
		return user.toDto();
	}
}
