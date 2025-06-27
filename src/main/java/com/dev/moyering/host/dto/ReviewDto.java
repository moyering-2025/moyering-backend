package com.dev.moyering.host.dto;

import java.sql.Date;

import javax.persistence.Column;

import org.springframework.web.multipart.MultipartFile;

import com.dev.moyering.host.entity.ClassCalendar;
import com.dev.moyering.host.entity.Host;
import com.dev.moyering.host.entity.Review;
import com.dev.moyering.user.entity.User;

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
	private String content;
	private Date reviewDate;
	private String revRegCotnent;
	private Date responseDate;
	private Integer star;
	private String reviewImgName;
	private Integer calendarId;
	private Integer state;
	private Integer userId;
	private Integer hostId;
	
	private String studentName;
	private String className;
	private String hostName;
	private String profileName; //리뷰 작성자 프사
	private String hostProfileName; //리뷰 답변자 (강사) 프사
   private MultipartFile reviewImg;

	public Review toEntity() {
		Review entity = Review.builder()
				.reviewId(reviewId)
				.content(content)
				.reviewDate(reviewDate)
				.revRegCotnent(revRegCotnent)
				.state(state)
				.responseDate(responseDate)
				.star(star)
				.reviewImg(reviewImgName)
				.build();
		if(hostId!=null) {
			entity.setHost(Host.builder()
					.hostId(hostId).build());
		}
		if(userId!=null) {
			entity.setUser(User.builder()
					.userId(userId)
					.build());
		}
		if(calendarId!=null) {
			entity.setClassCalendar(ClassCalendar.builder()
					.calendarId(calendarId)
					.build());
		}
		return entity;
	}
	
	
}
