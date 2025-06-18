package com.dev.moyering.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.dev.moyering.user.dto.UserDto;
import com.dev.moyering.user.entity.User;
import com.dev.moyering.user.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public void join(UserDto userDto) throws Exception {
		// 중복 아이디 확인
		if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
			throw new Exception("이미 존재하는 아이디입니다.");
		}

		// 유저 엔티티 생성 및 비밀번호 암호화
		User user = userDto.toEntity();
		user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));

		// 저장
		userRepository.save(user);
	}
	
	public UserDto findUserByUserId(Integer userId) throws Exception{
		User user = userRepository.findById(userId).orElseThrow(()->new Exception("멤버 조회 오류"));
		return user.toDto();
	}

}
