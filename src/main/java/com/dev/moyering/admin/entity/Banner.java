package com.dev.moyering.admin.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.dev.moyering.admin.dto.BannerDto;
import com.dev.moyering.common.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Banner {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer bannerId;
	@Column
	private String title;
	@Builder.Default
	@Column(columnDefinition = "INT DEFAULT 1")
	@Comment("1=게시, 0은 숨김")
	private Integer status = 1;
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @Column
    private String bannerImg;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="userId")
	private User user;

	public BannerDto toDto() {
		BannerDto dto = BannerDto.builder()
				.bannerId(bannerId)
				.title(title)
				.status(status)
				.createdAt(createdAt)
				.bannerImg(bannerImg)
				.build();
		if (user!=null)
			dto.setUserId(user.getUserId());
		return dto;
	}
}
