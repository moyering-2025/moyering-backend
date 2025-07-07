package com.dev.moyering.host.service;

import java.util.List;

import com.dev.moyering.host.dto.ClassRegistDto;

public interface ClassRegistService {
	List<ClassRegistDto> getRegisterdClassByUserId(Integer userId) throws Exception;

}
