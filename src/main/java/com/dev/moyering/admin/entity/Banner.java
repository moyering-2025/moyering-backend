package com.dev.moyering.admin.entity;

import java.time.LocalDateTime;

import javax.persistence.*;

import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.dev.moyering.admin.dto.BannerDto;
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
@EntityListeners(AuditingEntityListener.class)
public class Banner {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer bannerId;

	@Column
	private String title;

	@Column
	private String content;

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
				.bannerId(this.bannerId)
				.title(this.title)
				.content(this.content)
				.status(this.status)
				.createdAt(this.createdAt)
				.bannerImg(this.bannerImg)
				.build();
		if (user!=null)
			dto.setUserId(user.getUserId());
		return dto;
	}

	public void changeBanner(String title, String content, String img) {
		this.title = title;
		this.content = content;
		this.bannerImg = img;
	}

	public void deleteBanner() {Integer bannerId = this.bannerId;}

	public void hide(){
		this.status = 1;
	}


	public void show(){
		this.status = 0;
	}
}
