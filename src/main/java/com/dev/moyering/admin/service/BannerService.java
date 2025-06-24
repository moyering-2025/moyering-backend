package com.dev.moyering.admin.service;

import java.util.List;

import com.dev.moyering.admin.dto.BannerDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface BannerService {
	List<BannerDto> getMainBnanerList(Integer status) throws Exception;

	Page<BannerDto> findBannerByKeyword(String Keyword, Pageable pageable) throws Exception; // 리스트 조회

	BannerDto createBanner(BannerDto bannerDto, MultipartFile ifile) throws Exception; // 배너 생성

	BannerDto updateBanner(BannerDto bannerDto, MultipartFile ifile) throws Exception; // 배너 수정

	void deleteBanner(Integer bannerId) throws Exception; // 배너 삭제

	BannerDto findBannerByBannerId(Integer bannerId) throws Exception; // 단건 조회

	void hideBanner(Integer bannerId) throws Exception; // 배너 숨기기

	void showBanner(Integer bannerId) throws Exception; // 배너 보이기

	long countVisibleBanners();
}




