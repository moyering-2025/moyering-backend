package com.dev.moyering.host.service;

import java.sql.Date;
import java.util.List;

import com.dev.moyering.host.dto.HostClassDto;

public interface HostClassService {
	List<HostClassDto> getRecommendHostClassesForUser(Integer userId) throws Exception;
	Integer registClass(HostClassDto hostClassDto, List<Date> dates) throws Exception;
	
}
