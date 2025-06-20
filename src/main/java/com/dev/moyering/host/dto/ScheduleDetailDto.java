package com.dev.moyering.host.dto;

import java.sql.Timestamp;
import java.time.LocalTime;

import com.dev.moyering.host.entity.HostClass;
import com.dev.moyering.host.entity.ScheduleDetail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleDetailDto {
	private Integer scheduleId;
	private Integer classId;
	private String name;
	private String startTime;
	private String endTime;
	private String content;
	
	public ScheduleDetail toEntity() {
		ScheduleDetail entity = ScheduleDetail.builder()
				.scheduleId(scheduleId)
				.name(name)
				.startTime(LocalTime.parse(startTime))
				.endTime(LocalTime.parse(endTime))
				.content(content)
				.build();
		if(classId!=null) {
			entity.setHostClass(HostClass.builder()
					.classId(classId)
					.build());
		}
		return entity;
		
	}
	
}
