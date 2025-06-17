package com.dev.moyering.host.service;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.dev.moyering.common.entity.User;
import com.dev.moyering.common.repository.UserRepository;
import com.dev.moyering.host.dto.HostClassDto;
import com.dev.moyering.host.entity.HostClass;
import com.dev.moyering.host.repository.ClassCalendarRepository;
import com.dev.moyering.host.repository.HostClassRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HostClassServiceImpl implements HostClassService {
    private final HostClassRepository hostClassRepository;
    private final ClassCalendarRepository classCalendarRepository;
    private final UserRepository userRepository;

	@Override
	public List<HostClassDto> getRecommendHostClassesForUser(Integer userId) throws Exception {
	    List<HostClass> result;

		if (userId == null) {
	        result = hostClassRepository.findRecommendClassesForUser(null);
	    } else {
	        User user = userRepository.findById(userId)
	                .orElseThrow(() -> new Exception("해당 사용자를 찾을 수 없습니다: id=" + userId));
	        result = hostClassRepository.findRecommendClassesForUser(user);
	    }
		
		List<Integer> classIds = result.stream()
		        .map(HostClass::getClassId)
		        .collect(Collectors.toList());

	    Map<Integer, Date> startDateMap = classCalendarRepository.findEarliestStartDatesByClassIds(classIds);

        return result.stream()
        		.map(hostClass -> {
                    HostClassDto dto = hostClass.toDto();
                    dto.setStartDate(startDateMap.get(hostClass.getClassId()));
                    return dto;
                })
                .collect(Collectors.toList());
    }


	@Override
	public void registClass(HostClassDto hostClassDto) throws Exception {
		hostClassRepository.save(hostClassDto.toEntity());
	}
	
}
