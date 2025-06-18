package com.dev.moyering.host.service;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.dev.moyering.host.dto.HostClassDto;
import com.dev.moyering.host.entity.ClassCalendar;
import com.dev.moyering.host.repository.ClassCalendarRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClassCalendarServiceImpl implements ClassCalendarService {
    private final ClassCalendarRepository classCalendarRepository;

	@Override
	public List<HostClassDto> getHotHostClasses() throws Exception {
		List<ClassCalendar> classList = classCalendarRepository.findTop4ByDistinctHostClass();
		
		return classList.stream()
				.map(cal -> {
                    HostClassDto dto = cal.getHostClass().toDto();
                    dto.setStartDate(cal.getStartDate());
                    return dto;
                })
				.collect(Collectors.toList());
	}

}
