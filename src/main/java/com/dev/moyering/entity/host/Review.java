package com.dev.moyering.entity.host;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.dev.moyering.dto.host.ReviewDto;
import com.dev.moyering.entity.common.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Review {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer reviewId;
	@Column
	private String className;
	@Column
	private String content;
	@Column
	private String studentName;
	@Column
	private Integer state;
	@Column
	private String revRegCotnent;
	@Column
	private Date responseDate;
	@ManyToOne
	@JoinColumn(name="calendarId")
	private ClassCalendar classCalendar;
	@ManyToOne
	@JoinColumn(name="hostId")
	private Host host;
	@ManyToOne
	@JoinColumn(name="userId")
	private User user;
	
	ReviewDto toDto() {
		ReviewDto dto = ReviewDto.builder()
				.reviewId(reviewId)
				.className(className)
				.content(content)
				.state(state)
				.revRegCotnent(revRegCotnent)
				.responseDate(responseDate)
				.build();
		if(classCalendar!=null) {
			dto.setCalendarId(classCalendar.getCalendarId());
		}
		if(host!=null) {
			dto.setHostId(host.getHostId());			
		}
		if(user!=null) {
			dto.setUserId(host.getHostId());
			dto.setStudentName(host.getName());
		}
		return dto;
	}
	
}
