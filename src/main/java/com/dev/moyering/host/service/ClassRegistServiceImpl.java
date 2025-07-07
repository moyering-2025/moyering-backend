package com.dev.moyering.host.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.dev.moyering.host.dto.ClassRegistDto;
import com.dev.moyering.host.entity.ClassRegist;
import com.dev.moyering.host.repository.ClassRegistRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClassRegistServiceImpl implements ClassRegistService {
	private final ClassRegistRepository classRegistRepository;

	@Override
	public List<ClassRegistDto> getRegisterdClassByUserId(Integer userId) throws Exception {
		return classRegistRepository.findAllByUser_UserId(userId)
				.stream().map(cr-> ClassRegistDto.builder()
						.calendarId(cr.getClassCalendar().getCalendarId())
						.build()
				)
				.collect(Collectors.toList());
	}
}
