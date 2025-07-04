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


	/**
	 * 배너 검색 및 조회 + 페이징
	 */
	@Override
	public Page<BannerDto> findBannerByKeyword(String keyword, Pageable pageable) throws Exception {
		log.info("배너 검색 시작: keyword = {}", keyword);

		// Repository의 QueryDSL 메서드 호출
		Page<BannerDto> result = bannerRepository.findBannerByKeyword(keyword, pageable);

		log.info("배너 검색 완료: 총 {}개 조회", result.getTotalElements());
		return result;
	}

	/**
	 * 배너 생성
	 */
	@Transactional
	@Override
	public BannerDto createBanner(BannerDto bannerDto, MultipartFile ifile) throws Exception {
		// 배너 이미지 검증
		if (ifile != null && !ifile.isEmpty()) { // 배너이미지에 값이 있으면,
			log.info("파일 업로드 전 - bannerImg: {}", bannerDto.getBannerImg());
			bannerDto.setBannerImg (ifile.getOriginalFilename());
			log.info("파일 업로드 후 - bannerImg: {}", bannerDto.getBannerImg());
			File UpFile = new File(bannerUploadPath + ifile.getOriginalFilename());
			ifile.transferTo(UpFile);
		}
		try {
			// 날짜 수동 설정
			bannerDto.setCreatedAt(new java.sql.Date(System.currentTimeMillis()));
			log.info("=== 배너 등록 시작 ===");
			log.info("제목: {}, 내용 : {}, 등록일 : {}",
					bannerDto.getTitle(), bannerDto.getContent(), bannerDto.getCreatedAt());

			// 1. DTO -> Entity 변환
			Banner banner = bannerDto.toEntity();
			log.debug("DTO -> Entity 변환 완료!");

			// 2. 영속화
			Banner savedBanner = bannerRepository.save(banner);
			log.info("배너 저장 완료 = ID = {}", savedBanner.getBannerId());

			// 3. Entity -> DTO 변환 및 반환
			BannerDto result = savedBanner.toDto();
			log.info("배너 등록 완료 : ID = {}", savedBanner.getBannerId());
			return result;

		} catch (Exception e) {
			log.error("배너 등록 실패 : {}", e.getMessage(), e);
			throw e;
		}
	}




	/**
	 * 배너 수정  => 엔티티 내 changeBanner 메서드 호출하여 title, content, bannerImg 수정
	 */
	@Transactional
	@Override
	public BannerDto updateBanner(BannerDto bannerDto, MultipartFile ifile) throws Exception {
		log.info("배너수정 시작 : bannerId ={}", bannerDto.getBannerId());
		log.info("받은 파일 : {}", ifile != null ? ifile.getOriginalFilename() : "null");

		// 엔티티 조회 - orElseThrow 사용
		Banner banner = bannerRepository.findById(bannerDto.getBannerId())
				.orElseThrow(() -> new IllegalArgumentException("해당 배을 찾을 수 없습니다. ID : " + bannerDto.getBannerId()));

		// 파일업로드 추가
		if (ifile != null && !ifile.isEmpty()) { // 값이 있으면,
			log.info("파일 업로드 : {}", ifile.getOriginalFilename());

			// 새 파일 저장
			File uploadFile = new File(bannerUploadPath + ifile.getOriginalFilename());
			ifile.transferTo(uploadFile);

			// DTO에 파일명 설정
			bannerDto.setBannerImg(ifile.getOriginalFilename());
			log.info("DTO에 설정된 이미지: {}", bannerDto.getBannerImg());
		} else { // 값이 없으면 기존 저장된 파일 유지
			bannerDto.setBannerImg(banner.getBannerImg());
			log.info("기존이미지 유지 : {}", bannerDto.getBannerImg());
		}

		log.info("메서드 호출 전 : {}", bannerDto.getTitle(), bannerDto.getContent(), bannerDto.getBannerImg());
		// 비즈니스 메서드 호출해서 값 수정
		banner.changeBanner(bannerDto.getTitle(),
				bannerDto.getContent(),
				bannerDto.getBannerImg());

		log.info("메서드 호출 후 : {}", bannerDto.getTitle(), bannerDto.getContent(), bannerDto.getBannerImg());


		// save 호출
		Banner savedBanner = bannerRepository.save(banner);
		log.info("배너 수정 완료 : bannerId = {}", bannerDto.getBannerId());

		BannerDto result =  savedBanner.toDto();
		log.info("최종 DTO - bannerImg : {}", result.getBannerImg());
		return result;

	}

	/**
	 * 배너 삭제  => 엔티티 내 deleteBanner 메서드 호출하여 삭제
	 */
	@Transactional
	@Override
	public void deleteBanner(Integer bannerId) throws Exception {
		Banner banner = bannerRepository.findById(bannerId)
				.orElseThrow(() -> new IllegalArgumentException("배너가 존재하지 않습니다."));
		banner.deleteBanner(); // 논리적 삭제 처리하기 (엔티티 비즈니스 로직 활용)
	}



	/**
	 * 배너 단건 조회 	 */
	@Override
	public BannerDto findBannerByBannerId(Integer bannerId) throws Exception {
		Banner banner = bannerRepository.findById(bannerId)
				.orElseThrow(() -> new IllegalArgumentException("배너가 존재하지 않습니다"));
		return banner.toDto(); // 단건이므로 toDto()만 호출
	}


	/**
	 * 숨기기 => 엔티티 내 hide() 메서드 호출
	 */
	@Override
	public void hideBanner(Integer bannerId) throws Exception {
		Banner banner = bannerRepository.findById(bannerId)
				.orElseThrow(() -> new IllegalArgumentException("배너가 존재하지 않습니다."));

		// 배너 숨김처리 => 엔티티 내 비즈니스 로직 메서드 호출
		banner.hide() ;
		// 또는 banner.setStatus(BannerStatus.HIDDEN)
		bannerRepository.save(banner);
	}

	/**
	 * 보이기 => 엔티티 내 show() 메서드 호출
	 */
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

