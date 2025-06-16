package com.dev.moyering.host.service;

import java.util.List;

import com.dev.moyering.host.dto.HostClassDto;

public interface HostClassService {
	List<HostClassDto> getRecommendHostClassesForUser(Integer userId) throws Exception;
}
