package com.dev.moyering.host.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.moyering.host.dto.ScheduleDetailDto;
import com.dev.moyering.host.entity.ScheduleDetail;
import com.dev.moyering.host.repository.ScheduleDetailRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ScheduleDetailServiceImpl implements ScheduleDetailService {

	@Autowired
	private ScheduleDetailRepository scheduleDetailRespository;

	@Override
	public void registScheduleDetail(String scheduleDetail, Integer classId) throws Exception {
		List<ScheduleDetail> scheduleList = new ArrayList<>();
		ObjectMapper objMapper = new ObjectMapper();
		List<ScheduleDetailDto> scheduleDetails = objMapper.readValue(scheduleDetail,
				new TypeReference<List<ScheduleDetailDto>>() {
				});

		for (ScheduleDetailDto scheduleDetailDto : scheduleDetails) {
			scheduleDetailDto.setClassId(classId);
			scheduleList.add(scheduleDetailDto.toEntity());
		}
		for (ScheduleDetail schedule : scheduleList) {
			scheduleDetailRespository.save(schedule);
		}

	}

	@Override
	public List<ScheduleDetailDto> selectScheduleDetailByClassId(Integer classId) throws Exception {
		List<ScheduleDetail> scheduleDetail = scheduleDetailRespository.findByHostClassClassId(classId);
		List<ScheduleDetailDto> scheduleDetailDtoList = new ArrayList<>();
		for (ScheduleDetail schedule : scheduleDetail) {
			scheduleDetailDtoList.add(schedule.toDto());
		}
		return scheduleDetailDtoList;
	}

	@Override
	public void updateScheduleDetail(String scheduleDetail, Integer classId) throws Exception {
		List<ScheduleDetail> scheduleList = new ArrayList<>();
		ObjectMapper objMapper = new ObjectMapper();
		List<ScheduleDetailDto> scheduleDetails = objMapper.readValue(scheduleDetail,
				new TypeReference<List<ScheduleDetailDto>>() {
				});
		
		
		List<ScheduleDetail> sdList = scheduleDetailRespository.findByHostClassClassId(classId);
		List<ScheduleDetailDto> sddList = new ArrayList<>();
		for(ScheduleDetail sd : sdList) {
			sddList.add(sd.toDto());
		}
		
//		for()
		
		
		for (ScheduleDetailDto scheduleDetailDto : scheduleDetails) {
			scheduleDetailDto.setClassId(classId);
			scheduleList.add(scheduleDetailDto.toEntity());
		}
		for (ScheduleDetail schedule : scheduleList) {
			scheduleDetailRespository.save(schedule);
		}

	}

}
