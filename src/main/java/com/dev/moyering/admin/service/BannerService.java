package com.dev.moyering.admin.service;

import java.util.List;

import com.dev.moyering.admin.dto.AdminNoticeDto;
import com.dev.moyering.admin.dto.BannerDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BannerService {
	List<BannerDto> getMainBnanerList(Integer status) throws Exception;

	Page<BannerDto> findBannerByKeyword(String Keyword, Pageable pageable) throws Exception; // 리스트 조회

	BannerDto createBanner(BannerDto bannerDto) throws Exception; // 배너 생성

	BannerDto updateBanner(Integer BannerId, BannerDto bannerDto) throws Exception; // 배너 수정
	BannerDto deleteBanner(Integer bannerId) throws Exception; // 배너 삭제
	BannerDto findBannerByBannerId(Integer bannerId) throws Exception;
	void hideNotice(Integer bannerId) throws Exception;

	void showNotice(Integer bannerId) throws Exception;

}




