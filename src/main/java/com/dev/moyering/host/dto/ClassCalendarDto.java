package com.dev.moyering.host.dto;

import java.sql.Date;
import java.time.LocalTime;

import com.dev.moyering.host.entity.ClassCalendar;
import com.dev.moyering.host.entity.HostClass;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassCalendarDto {
	private Integer calendarId;
	private Integer classId;
	private Date startDate;
	private Date endDate;
	private String status;
	private Integer registeredCount;
	
	private String className;
    private LocalTime startTime;

	public ClassCalendar toEntity() {
		ClassCalendar entity = ClassCalendar.builder()
				.calendarId(calendarId)
				.startDate(startDate)
				.endDate(endDate)
				.status(status)
				.registeredCount(registeredCount)
				.build();
		if(classId!=null) {
			entity.setHostClass(HostClass.builder()
					.classId(classId)
					.build());
		}
		return entity;
	}
}
