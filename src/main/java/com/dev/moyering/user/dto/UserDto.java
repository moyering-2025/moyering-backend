package com.dev.moyering.user.dto;

import java.sql.Date;

import com.dev.moyering.user.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
	private Integer userId;
	private String username;//아이디인데  security규약에 의해서 userName으로 했음.
	private String profile;
	private String password;
	private String name;
	private String nickName;
	private String tel;
	private Date birthday;
	private String addr;
	private String detailAddr;
	private Double latitude;
	private Double longitude;
	private String category1;
	private String category2;
	private String category3;
	private String category4;
	private String category5;
	private String intro;
	private String fcmToken;
	private String useYn;
	private String userType;
	private String email;
	private Date regDate;
	private String provider;
	private String providerId;
	private Integer activeScore;
	private Integer userBadgeId;
	private Boolean emailVerified;
	private String emialVerificationToken;
	private String badgeImg;

	public User toEntity() {
		User entity = User.builder()
				.userId(userId)
				.username(username)
				.profile(profile)
				.password(password)
				.name(name)
				.nickName(nickName)
				.tel(tel)
				.birthday(birthday)
				.addr(addr)
				.detailAddr(detailAddr)
				.latitude(latitude)
				.longitude(longitude)
				.category1(category1)
				.category2(category2)
				.category3(category3)
				.category4(category4)
				.category5(category5)
				.intro(intro)
				.fcmToken(fcmToken)
				.useYn(useYn)
				.userType(userType)
				.email(email)
				.regDate(regDate)
				.provider(provider)
				.providerId(providerId)
				.activeScore(activeScore)
				.userBadgeId(userBadgeId)
				.emailVerified(emailVerified)
				.build();
		
		return entity;
	}
	public UserDto(Integer userId, String username, String nickName, String profile) {
		this.userId = userId;
		this.username = username;
		this.nickName = nickName;
		this.profile = profile;

	}
}
