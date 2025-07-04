package com.dev.moyering.user.service;

import com.dev.moyering.admin.dto.AdminMemberDto;
import com.dev.moyering.admin.dto.AdminMemberSearchCond;
import com.dev.moyering.user.dto.UserBadgeDto;
import com.dev.moyering.user.dto.UserDto;
import com.dev.moyering.user.dto.UserProfileDto;
import com.dev.moyering.user.dto.UserProfileUpdateDto;
import com.dev.moyering.user.entity.User;
import com.dev.moyering.user.entity.UserBadge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    void join(UserDto userDto) throws Exception;

    UserDto findUserByUserId(Integer userId) throws Exception;

    User findUserByUsername(String username) throws Exception;

    // 관리자페이지 > 회원 조회
    Page<AdminMemberDto> getMemberList(AdminMemberSearchCond cond, Pageable pageable) throws Exception;

    // 관리자페이지 > 회원상태 변경(활성, 비활성)
    void updateMemberStatus(Integer userId, String status);

    // 관리자페이지 > 회원조회 > 상세 조회 페이지
    AdminMemberDto getMemberDetail(Integer userId);

    void updateUserRole(Integer userId) throws Exception;

    //feed
    UserDto getByNickname(String nickname) throws Exception;

    Boolean verifyEmail(String token) throws Exception;

    void completeJoin(UserDto userDto) throws Exception;

    //유저 프로필 업데이트
    void updateUserProfile(Integer userId, UserProfileUpdateDto dto, MultipartFile profileImage) throws Exception;
    UserProfileDto getMyProfile(Integer userId) throws Exception;

    List<UserBadgeDto> getUserBadges(Integer userId);
    void updateRepresentativeBadge(Integer userId, Integer userBadgeId) throws Exception;

    UserBadge getUserFirstBadge(Integer userId);
}
