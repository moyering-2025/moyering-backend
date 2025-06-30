package com.dev.moyering.host.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;

import com.dev.moyering.host.dto.ClassCalendarDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@DynamicInsert
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
	@Comment("클래스 일정 상태 (승인대기, 임시저장, 반려, 모집중,모집마감,개강,폐강,운영종료)")
	private String status;
	@Column
	@ColumnDefault("0")
	private Integer registeredCount;
	
	public ClassCalendarDto toDto() {
		ClassCalendarDto dto = ClassCalendarDto.builder()
				.calendarId(calendarId)
				.startDate(startDate)
				.endDate(endDate)
				.status(status)
				.registeredCount(registeredCount)
				.build();
		if(hostClass!=null) {
			dto.setClassId(hostClass.getClassId());
		}
		
		return dto;
	}
	
	//등록인원 1추가
	public void incrementRegisteredCount() {
		this.registeredCount++;
	}
}
