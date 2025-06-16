package com.dev.moyering.entity.host;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.dev.moyering.dto.host.ClassCalendarDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class ClassCalendar {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer calendarId;
	@ManyToOne
	@JoinColumn(name="classId")
	private HostClass hostClass;
	@Column
	private Date startDate;
	@Column
	private Date endDate;
	@Column
	private String status;
	
	public ClassCalendarDto toDeto() {
		ClassCalendarDto dto = ClassCalendarDto.builder()
				.calendarId(calendarId)
				.startDate(startDate)
				.endDate(endDate)
				.status(status)
				.build();
		if(hostClass!=null) {
			dto.setClassId(hostClass.getClassId());
		}
		
		return dto;
	}
}
