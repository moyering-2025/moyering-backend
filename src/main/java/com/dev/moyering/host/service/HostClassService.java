package com.dev.moyering.host.service;

import java.util.List;

import com.dev.moyering.common.dto.ClassSearchRequestDto;
import com.dev.moyering.common.dto.ClassSearchResponseDto;
import com.dev.moyering.host.dto.HostClassDto;

public interface HostClassService {
	List<HostClassDto> getRecommendHostClassesForUser(Integer userId) throws Exception;
	void registClass(HostClassDto hostClassDto) throws Exception;
	ClassSearchResponseDto searchClasses(ClassSearchRequestDto dto) throws Exception;
}
