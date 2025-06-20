package com.dev.moyering.host.entity;

import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.dev.moyering.host.dto.ScheduleDetailDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class ScheduleDetail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer scheduleId;
	@OneToOne
	@JoinColumn(name = "classId")
	private HostClass hostClass;
	@Column
	private String name;
	@Column
	private LocalTime startTime;
	@Column
	private LocalTime endTime;
	@Column
	private String content;

	public ScheduleDetailDto toDto() {
		ScheduleDetailDto dto = ScheduleDetailDto.builder()
				.scheduleId(scheduleId)
				.name(name)
				.startTime(startTime)
				.endTime(endTime)
				.content(content)
				.build();
		if (hostClass != null) {
			dto.setClassId(hostClass.getClassId());
		}
		return dto;
	}

}
