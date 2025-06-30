package com.dev.moyering.host.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.dev.moyering.host.dto.ReviewDto;
import com.dev.moyering.user.entity.User;

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
    @Column(nullable = false)
	private String content; //리뷰 
	@Column
	private Date reviewDate; //리뷰 날짜 
	@Column
	private String revRegCotnent; //리뷰 답변
	@Column
	private Integer state;
	@Column
	private Date responseDate; //리뷰 답변일자
	@Column
	private Integer star; //별점
	@Column
	private String reviewImg;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="calendarId")
	private ClassCalendar classCalendar;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="userId")
	private User user;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="hostId")
	private Host host;
	
	public ReviewDto toDto() {
		ReviewDto dto = ReviewDto.builder()
				.reviewId(reviewId)
				.content(content)
				.reviewDate(reviewDate)
				.revRegCotnent(revRegCotnent)
				.state(state)
				.responseDate(responseDate)
				.star(star)
				.reviewImgName(reviewImg)
				.build();
		if(classCalendar!=null) {
			dto.setCalendarId(classCalendar.getCalendarId());
			dto.setClassName(classCalendar.getHostClass().getName());
		}
		if(host!=null) {
			dto.setHostId(host.getHostId());	
			dto.setHostName(host.getName());
			dto.setHostProfileName(host.getProfile());
		}
		if(user!=null) {
			dto.setUserId(user.getUserId());
			dto.setStudentName(user.getName());
			dto.setProfileName(user.getProfile());
		}
		return dto;
	}
	
}
