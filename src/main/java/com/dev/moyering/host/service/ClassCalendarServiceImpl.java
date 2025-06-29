package com.dev.moyering.host.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.dev.moyering.host.dto.ClassCalendarDto;
import com.dev.moyering.host.dto.HostClassDto;
import com.dev.moyering.host.entity.ClassCalendar;
import com.dev.moyering.host.repository.ClassCalendarRepository;
import com.dev.moyering.user.entity.User;
import com.dev.moyering.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClassCalendarServiceImpl implements ClassCalendarService {
	private final ClassCalendarRepository classCalendarRepository;
	private final UserRepository userRepository;

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

	@Override
	public List<HostClassDto> getRecommendHostClassesForUser2(Integer userId) throws Exception {
		List<ClassCalendar> classes;
		if (userId == null) {
			classes = classCalendarRepository.findRecommendClassesForUser2(null);
		} else {
			User user = userRepository.findById(userId)
					.orElseThrow(() -> new Exception("해당 사용자를 찾을 수 없습니다: id=" + userId));
			classes = classCalendarRepository.findRecommendClassesForUser2(user);
		}
		List<HostClassDto> results;
		return classes.stream()
				.map(cc -> {
					HostClassDto dto = cc.getHostClass().toDto();
					dto.setStartDate(cc.getStartDate());
					return dto;
				})
				.collect(Collectors.toList());
	}

	@Override
	public List<ClassCalendarDto> selectCalednarByClassId(Integer classId) throws Exception {
		List<ClassCalendar> calendarList = classCalendarRepository.findByHostClassClassId(classId);
		List<ClassCalendarDto> calDtoList = new ArrayList<>();
		for(ClassCalendar cal : calendarList) {
			calDtoList.add(cal.toDto());
		}
		return calDtoList;
	}
	public List<ClassCalendarDto> getClassCalendarByHostClassId(Integer classId) {
		List<ClassCalendar> classes = classCalendarRepository.findAllByHostClass_ClassIdAndStatus(classId, "모집중");
		return classes.stream()
				.map(cc -> cc.toDto()).collect(Collectors.toList());
	}

}
