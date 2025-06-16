package com.dev.moyering.dto.host;

import java.sql.Date;

import com.dev.moyering.entity.common.User;
import com.dev.moyering.entity.host.Host;
import com.dev.moyering.entity.host.Review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDto {
	private Integer reviewId;
	private String className;
	private String content;
	private Integer state;
	private String revRegCotnent;
	private Date responseDate;
	private Integer calendarId;
	private Integer hostId;
	private Integer userId;
	private String studentName;
	
	public Review toEntity() {
		Review entity = Review.builder()
				.reviewId(reviewId)
				.className(className)
				.content(content)
				.state(state)
				.revRegCotnent(revRegCotnent)
				.responseDate(responseDate)
				.build();
		if(hostId!=null) {
			entity.setHost(Host.builder()
					.hostId(hostId).build());
		}
		if(userId!=null) {
			entity.setUser(User.builder()
					.userId(userId)
					.name(studentName)
					.build());
		}
		return entity;
	}
	
	
}
