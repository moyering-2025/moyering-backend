package com.dev.moyering.user.service;

import com.dev.moyering.admin.dto.AdminMemberDto;
import com.dev.moyering.admin.dto.AdminMemberSearchCond;
import com.dev.moyering.common.service.EmailService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.dev.moyering.user.dto.UserDto;
import com.dev.moyering.user.entity.User;
import com.dev.moyering.user.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired 
	private EmailService emailService;

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

		//이메일 인증을 위한 토큰 생성 및 초기 인증false
		String token = UUID.randomUUID().toString();
		user.setEmailVerificationToken(token);
		user.setEmailVerified(false);
		
		// 저장
		userRepository.save(user);
		
		emailService.sendVerificationEmail(user.getEmail(), token);
	}
	
	@Override
	public Boolean verifyEmail(String token) throws Exception {
		User user = userRepository.findByEmailVerificationToken(token).get();
		if(user !=null) {
			user.setEmailVerified(true);
			user.setEmailVerificationToken(null);
			userRepository.save(user);
			return true;
		}
		return false;
	}
	
	@Override
	public void completeJoin(UserDto userDto) throws Exception {
		User user = userRepository.findByUsername(userDto.getUsername()).get();
		
		if(user!=null && user.getEmailVerified()) {
			user.setCategory1(userDto.getCategory1());
			user.setCategory2(userDto.getCategory2());
			user.setCategory3(userDto.getCategory3());
			user.setCategory4(userDto.getCategory4());
			user.setCategory5(userDto.getCategory5());
			user.setIntro(userDto.getIntro());
		}else {
			throw new Exception("이메일 인증이 완료되지 않았습니다.");
		}
		
		userRepository.save(user);
	}

	
	

	public UserDto findUserByUserId(Integer userId) throws Exception {
		User user = userRepository.findById(userId).orElseThrow(() -> new Exception("멤버 조회 오류"));
		return user.toDto();
	}

	@Override
	public User findUserByUsername(String username) throws Exception {
		return userRepository.findByUsername(username).orElseThrow(() -> new Exception("멤버 조회 오류"));

	}
	// 관리자페이지 > 회원 관리 (검색 조회)
	@Override
	public Page<AdminMemberDto> getMemberList(AdminMemberSearchCond cond, Pageable pageable) throws Exception {
		log.info("getMemberList 호출 - cond : {}, pageable : {}", cond, pageable);
		try {
			List<AdminMemberDto> content = userRepository.searchMembers(cond, pageable);
			log.info("조회된 content 개수 : {}", content.size());

			Long total = userRepository.countMembers(cond); // 페이지 계산
			log.info("전체 개수: {}", total);

			return new PageImpl<>(content, pageable, total); // 내용, 페이지 수, 총 몇건인지
		} catch(Exception e){
			log.error("getMemberList 에러 상세 : ", e);
			throw e;
		}

	}

	// 관리자 페이지 > 회원 관리 > 상태 변경 (계정 활성화 / 비활성화)
	@Transactional
	@Override
	public void updateMemberStatus(Integer userId, String status) {
		log.info("회원 상태 변경 요청 - ID : {}, 변경할 상태 : {}", userId, status);
		try {
			// 1. 회원 존재 여부 확인 (UserDto -> User)
			User user = userRepository.findById(userId)
					.orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다 : " + userId));

			// 2. 회원 상태와 동일한지 확인 (활성화 -> 비활성화, 비활성화 -> 활성화)
			if (status.equals(user.getUseYn())) {
				log.warn("현재 상태와 동일한 상태로 변경 시도 - ID : {}, 상태 : {}", userId, status);
				return ; // 동일한 상태면 변경하지 않음
			}

			// 3. 상태 변경
			user.setUseYn(status);

			// 4. DB 저장
			userRepository.save(user); // 리파지토리는 엔티티만 처리
			log.info("회원 상태 변경 완료 - ID : {}, 이전 상태 : {}, 변경된 상태 : {}", userId, status, user);

		} catch (Exception e){
			log.error("회원상태 변경 실패 : {}", e.getMessage());
		}
	}

	@Override
	public AdminMemberDto getMemberDetail(Integer userId) {
		return null;
	}

	@Override
	public void updateUserRole(Integer userId) throws Exception {
		User user = userRepository.findById(userId).get();
		if(userId != null) {
			user.setUserType("ROLE_HT");
		}
		userRepository.save(user);
	}
	
	//feed
	@Override
	public UserDto getByNickname(String nickname) throws Exception {
		User user = userRepository.findByNickName(nickname)
				.orElseThrow(() -> new IllegalArgumentException("없는 닉네임입니다: " + nickname));
		return user.toDto();
	}

	


	
}