package com.dev.moyering.admin.dto;

import java.sql.Date;
import java.time.LocalDateTime;

import com.dev.moyering.admin.entity.Banner;
import com.dev.moyering.user.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BannerDto {
	private Integer bannerId; // 배너아이디
	private String bannerImg; // 배너이미지
	private Date createdAt; // 등록일자
	private String title; // 제목
	private String content; // 내용
	private Integer status; // 상태 (보이기 : 1, 숨기기 : 0)
	private Integer userId; // 등록아이디
	
	public com.dev.moyering.admin.entity.Banner toEntity() {
		com.dev.moyering.admin.entity.Banner entity = com.dev.moyering.admin.entity.Banner.builder()
				.bannerId(bannerId)
				.title(title)
				.content(content)
				.status(status)
				.bannerImg(bannerImg)
				.createdAt(createdAt)
				.build();
		if (userId !=null) {
			entity.setUser(User.builder()
					.userId(userId)
					.build());
		}
		return entity;
	}
}
