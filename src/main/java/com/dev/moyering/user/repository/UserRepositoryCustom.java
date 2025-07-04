package com.dev.moyering.user.repository;

import com.dev.moyering.admin.dto.AdminMemberDto;
import com.dev.moyering.admin.dto.AdminMemberSearchCond;

import com.dev.moyering.user.entity.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserRepositoryCustom {
    // 관리자페이지 회원관리 검색
    List<AdminMemberDto> searchMembers(AdminMemberSearchCond cond, Pageable pageable) ;

    // 검색 후 조회 건수
    Long countMembers(AdminMemberSearchCond cond);
}


