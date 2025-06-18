package com.dev.moyering.admin.dto;

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
	private Integer bannerId;
	private String title;
	private Integer status;
    private LocalDateTime createdAt;
    private String bannerImg;
	private Integer userId;
	
	public Banner toEntity() {
		Banner entity = Banner.builder()
				.bannerId(bannerId)
				.title(title)
				.status(status)
				.build();
		if (userId !=null) {
			entity.setUser(User.builder()
					.userId(userId)
					.build());
		}
		return entity;
	}
}
