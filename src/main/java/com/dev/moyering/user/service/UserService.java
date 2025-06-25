package com.dev.moyering.user.service;

import com.dev.moyering.admin.dto.AdminMemberDto;
import com.dev.moyering.admin.dto.AdminMemberSearchCond;
import com.dev.moyering.user.dto.UserDto;
import com.dev.moyering.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
	void join(UserDto userDto) throws Exception;
	UserDto findUserByUserId(Integer userId) throws Exception;
	User  findUserByUsername(String username) throws Exception;

	// 관리자페이지 > 회원 조회
	Page<AdminMemberDto> getMemberList(AdminMemberSearchCond cond, Pageable pageable) throws Exception;
	// 관리자페이지 > 회원상태 변경(활성, 비활성)
	void updateMemberStatus(Integer userId, String status);
	// 관리자페이지 > 회원조회 > 상세 조회 페이지
	AdminMemberDto getMemberDetail(Integer userId);
	void updateUserRole(Integer userId)throws Exception;
}
