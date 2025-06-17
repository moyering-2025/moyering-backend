package com.dev.moyering.admin.service;

import java.util.List;

import com.dev.moyering.admin.dto.BannerDto;

public interface BannerService {
	List<BannerDto> getMainBnanerList(Integer status) throws Exception;
}
