package com.dev.moyering.host.service;

import java.sql.Date;
import java.util.List;

import com.dev.moyering.common.dto.ClassSearchRequestDto;
import com.dev.moyering.common.dto.ClassSearchResponseDto;
import com.dev.moyering.host.dto.HostClassDto;

public interface HostClassService {
	List<HostClassDto> getRecommendHostClassesForUser(Integer userId) throws Exception;
	Integer registClass(HostClassDto hostClassDto, List<Date> dates) throws Exception;
	ClassSearchResponseDto searchClasses(ClassSearchRequestDto dto) throws Exception;
}
