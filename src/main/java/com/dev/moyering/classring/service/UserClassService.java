package com.dev.moyering.classring.service;

import com.dev.moyering.common.dto.ClassRingDetailResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface UserClassService {
	ClassRingDetailResponseDto getClassRingDetail(Integer classId, Integer userId) throws Exception;
}
