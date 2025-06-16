package com.dev.moyering.host.dto;

import java.sql.Timestamp;

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
	private Timestamp startTime;
	private Timestamp endTime;
	private String content;
	
	public ScheduleDetail toEntity() {
		ScheduleDetail entity = ScheduleDetail.builder()
				.scheduleId(scheduleId)
				.name(name)
				.startTime(startTime)
				.endTime(endTime)
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
