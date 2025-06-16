package com.dev.moyering.dto.common;

import java.sql.Date;

import com.dev.moyering.entity.common.User;

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
	private String id;
	private String profile;
	private String password;
	private String name;
	private String tel;
	private Date birthday;
	private String addr;
	private String detailAddr;
	private double latitude;
	private double longitude;
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
	private String proviederId;
	private Integer activeScore;
	private Integer userBadgeId;
	
	public User toEntity() {
		User entity = User.builder()
				.userId(userId)
				.id(id)
				.profile(profile)
				.password(password)
				.name(name)
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
				.proviederId(proviederId)
				.activeScore(activeScore)
				.userBadgeId(userBadgeId)
				.build();
		
		return entity;
	}
}
