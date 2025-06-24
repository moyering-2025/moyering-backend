package com.dev.moyering.admin.repository;

import com.dev.moyering.admin.dto.AdminNoticeDto;
import com.dev.moyering.admin.dto.BannerDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BannerRepositoryCustom {
    // 목록 조회 및 검색
    Page<BannerDto> findBannerByKeyword(String keyword, Pageable pageable); // 배너아이디, 제목 검색
    BannerDto findBannerByBannerId(Integer bannerId);
    long countVisibleBanners();
}
