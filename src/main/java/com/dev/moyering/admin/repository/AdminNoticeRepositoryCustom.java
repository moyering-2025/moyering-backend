// src/main/java/com/dev/moyering/repository/AdminNoticeRepositoryCustom.java
package com.dev.moyering.admin.repository;

import com.dev.moyering.admin.dto.AdminNoticeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminNoticeRepositoryCustom {
    // 목록 조회 및 검색
    Page<AdminNoticeDto> findNoticesByKeyword(String searchKeyword, Pageable pageable);
    AdminNoticeDto findNoticeByNoticeId(Integer noticeId);
}