// src/main/java/com/dev/moyering/repository/AdminNoticeRepositoryCustom.java
package com.dev.moyering.admin.repository;

import com.dev.moyering.admin.dto.NoticeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeRepositoryCustom {
    // 목록 조회 및 검색
    Page<NoticeDto> findNoticesByKeyword(String searchKeyword, Pageable pageable);
}