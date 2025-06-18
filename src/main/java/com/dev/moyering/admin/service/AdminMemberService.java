package com.dev.moyering.admin.service;

import com.dev.moyering.admin.dto.AdminMemberDto;
import com.dev.moyering.admin.dto.AdminMemberSearchCond;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;

public interface AdminMemberService {
    Page <AdminMemberDto> searchMembers (AdminMemberSearchCond cond, Pageable pageable);
}
