package com.dev.moyering.admin.service;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import com.dev.moyering.admin.dto.BannerDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dev.moyering.admin.entity.Banner;
import com.dev.moyering.admin.repository.BannerRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor
@Slf4j
public class BannerServiceImpl implements BannerService {
	private final BannerRepository bannerRepository;

	// 배너 경로
	@Value("${iupload.path}")
	private String bannerUploadPath;

	@Override
	public List<BannerDto> getMainBnanerList(Integer status) throws Exception {
		List<Banner> banners = bannerRepository.findByStatus(status);
		return banners.stream().map(m->m.toDto()).collect(Collectors.toList());
	}


	/*** 배너 검색 및 조회 + 페이징*/
	@Override
	public Page<BannerDto> findBannerByKeyword(String keyword, Pageable pageable) throws Exception {
		Page<BannerDto> result = bannerRepository.findBannerByKeyword(keyword, pageable);
		return result;
	}

	/*** 배너 생성*/
	@Transactional
	@Override
	public BannerDto createBanner(BannerDto bannerDto, MultipartFile ifile) throws Exception {
		// 배너 이미지 검증
		if (ifile != null && !ifile.isEmpty()) { // 배너이미지에 값이 있으면,
			bannerDto.setBannerImg (ifile.getOriginalFilename());
			File UpFile = new File(bannerUploadPath + ifile.getOriginalFilename());
			ifile.transferTo(UpFile);
		}
		try {
			bannerDto.setCreatedAt(new java.sql.Date(System.currentTimeMillis()));
			Banner banner = bannerDto.toEntity(); // 1. DTO -> Entity 변환
			Banner savedBanner = bannerRepository.save(banner); // 2. 영속화
			BannerDto result = savedBanner.toDto();// 3. Entity -> DTO 변환 및 반환
			return result;
		} catch (Exception e) {
			throw e;
		}
	}


	/*** 배너 수정  => 엔티티 내 changeBanner 메서드 호출하여 title, content, bannerImg 수정*/
	@Transactional
	@Override
	public BannerDto updateBanner(BannerDto bannerDto, MultipartFile ifile) throws Exception {
		Banner banner = bannerRepository.findById(bannerDto.getBannerId())
				.orElseThrow(() -> new IllegalArgumentException("해당 배을 찾을 수 없습니다. ID : " + bannerDto.getBannerId()));

		if (ifile != null && !ifile.isEmpty()) { // 값이 있으면,
			File uploadFile = new File(bannerUploadPath + ifile.getOriginalFilename()); // 새 파일 저장
			ifile.transferTo(uploadFile);
			bannerDto.setBannerImg(ifile.getOriginalFilename());
		} else { // 값이 없으면 기존 저장된 파일 유지
			bannerDto.setBannerImg(banner.getBannerImg());
		}

		// 비즈니스 메서드 호출해서 값 수정
		banner.changeBanner(bannerDto.getTitle(),
				bannerDto.getContent(),
				bannerDto.getBannerImg());
		Banner savedBanner = bannerRepository.save(banner);
		BannerDto result =  savedBanner.toDto();
		return result;

	}

	/*** 배너 삭제  => 엔티티 내 deleteBanner 메서드 호출하여 삭제*/
	@Transactional
	@Override
	public void deleteBanner(Integer bannerId) throws Exception {
		Banner banner = bannerRepository.findById(bannerId)
				.orElseThrow(() -> new IllegalArgumentException("배너가 존재하지 않습니다."));
		banner.deleteBanner(); // 논리적 삭제 처리하기 (엔티티 비즈니스 로직 활용)
	}


	/*** 배너 단건 조회*/
	@Override
	public BannerDto findBannerByBannerId(Integer bannerId) throws Exception {
		Banner banner = bannerRepository.findById(bannerId)
				.orElseThrow(() -> new IllegalArgumentException("배너가 존재하지 않습니다"));
		return banner.toDto(); // 단건이므로 toDto()만 호출
	}


	/*** 숨기기 => 엔티티 내 hide() 메서드 호출*/
	@Override
	public void hideBanner(Integer bannerId) throws Exception {
		Banner banner = bannerRepository.findById(bannerId)
				.orElseThrow(() -> new IllegalArgumentException("배너가 존재하지 않습니다."));
		banner.hide() ; // 배너 숨김처리 => 엔티티 내 비즈니스 로직 메서드 호출
		bannerRepository.save(banner);
	}

	/*** 보이기 => 엔티티 내 show() 메서드 호출*/
	@Override
	public void showBanner(Integer bannerId) throws Exception {
		Banner banner = bannerRepository.findById(bannerId)
				.orElseThrow(() -> new IllegalArgumentException("배너가 존재하지 않습니다.."));
		banner.show();
		bannerRepository.save(banner);
	}

	public long countVisibleBanners() {
		return bannerRepository.countVisibleBanners();
	}
}

