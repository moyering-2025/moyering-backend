package com.dev.moyering.host.service;

import java.util.List;

import com.dev.moyering.host.dto.ClassRegistDto;
import com.dev.moyering.host.entity.ClassRegist;

public interface ClassRegistService {
	List<ClassRegistDto> getRegisterdClassByUserId(Integer userId) throws Exception;
	List<ClassRegist> getregisterdUsersByClassId(Integer calendarId) throws Exception;

}
