package com.dev.moyering.admin.service;

import java.util.List;
import java.util.stream.Collectors;

import com.dev.moyering.admin.dto.BannerDto;
import com.dev.moyering.admin.entity.AdminNotice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dev.moyering.admin.entity.Banner;
import com.dev.moyering.admin.repository.BannerRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import static com.dev.moyering.admin.entity.QBanner.banner;

@Service
@RequiredArgsConstructor
@Slf4j
public class BannerServiceImpl implements BannerService {
	private final BannerRepository bannerRepository;

	@Override
	public List<BannerDto> getMainBnanerList(Integer status) throws Exception {
		List<Banner> banners = bannerRepository.findByStatus(status);
		return banners.stream().map(m->m.toDto()).collect(Collectors.toList()); 
	}

	@Override
	public Page<BannerDto> findBannerByKeyword(String keyword, Pageable pageable) throws Exception {
		log.info("배너 검색 시작: keyword = {}", keyword);

		// Repository의 QueryDSL 메서드 호출
		Page<BannerDto> result = bannerRepository.findBannerByKeyword(keyword, pageable);

		log.info("배너 검색 완료: 총 {}개 조회", result.getTotalElements());
		return result;
	}

	@Transactional
	@Override
	public BannerDto createBanner(BannerDto bannerDto) throws Exception {
			try {
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


	@Override
	public BannerDto updateBanner(Integer bannerId, BannerDto bannerDto) throws Exception {
		log.info("배너수정 시작 : bannerId ={}", bannerId);

		// 엔티티 조회 - orElseThrow 사용
		Banner banner = bannerRepository.findById(bannerId)
				.orElseThrow(() -> new IllegalArgumentException("해당 배을 찾을 수 없습니다. ID : " + bannerId));

		// 비즈니스 메서드 호출해서 값 수정
		banner.changeBanner(bannerDto.getTitle(),
				bannerDto.getContent(),
				bannerDto.getBannerImg());
		log.info("배너 수정 완료 : bannerId = {}", bannerId);
		return banner.toDto();
	}


	@Override
	public BannerDto deleteBanner(Integer bannerId) throws Exception {
		return null;
	}

	@Override
	public BannerDto findBannerByBannerId(Integer bannerId) throws Exception {
		return null;
	}

	@Override
	public void hideNotice(Integer bannerId) throws Exception {

	}

	@Override
	public void showNotice(Integer bannerId) throws Exception {

	}


}
