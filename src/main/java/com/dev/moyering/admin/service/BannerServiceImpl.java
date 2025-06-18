package com.dev.moyering.admin.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.dev.moyering.admin.dto.BannerDto;
import com.dev.moyering.admin.entity.Banner;
import com.dev.moyering.admin.repository.BannerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BannerServiceImpl implements BannerService {
	private final BannerRepository bannerRepository;
	@Override
	public List<BannerDto> getMainBnanerList(Integer status) throws Exception {
		List<Banner> banners = bannerRepository.findByStatus(status);
		return banners.stream().map(m->m.toDto()).collect(Collectors.toList()); 
	}

}
