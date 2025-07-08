package com.dev.moyering.common.service;

import java.util.Map;

import com.dev.moyering.common.dto.MainSearchRequestDto;

public interface MainService {
	Map<String,Object> searchAllBySearchQuery(MainSearchRequestDto dto)throws Exception;

}
