package com.dev.moyering.admin.dto;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.data.annotation.CreatedDate;

import com.dev.moyering.common.entity.User;

public class BannerDto {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer bannerId;
	@Column
	private String title;
	@Column
	private String status;
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @Column
    private String bannerImg;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="userId")
	private User user;
}
