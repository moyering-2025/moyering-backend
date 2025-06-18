package com.dev.moyering.host.service;

import java.util.List;

import com.dev.moyering.host.dto.HostClassDto;

public interface ClassCalendarService {
	List<HostClassDto> getHotHostClasses() throws Exception;
}
