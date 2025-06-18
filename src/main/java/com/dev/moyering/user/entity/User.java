package com.dev.moyering.user.entity;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.dev.moyering.user.dto.UserDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class User {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer userId;
	@Column
	private String username;//아이디인데  security규약에 의해서 userName으로 했음.
	@Column
	private String profile;
	@Column
	private String password;
	@Column
	private String name;
	@Column
	private String nickName;
	@Column
	private String tel;
	@Column
	private Date birthday;
	@Column
	private String addr;
	@Column
	private String detailAddr;
	@Column
	private Double latitude;
	@Column
	private Double longitude;
	@Column
	private String category1;
	@Column
	private String category2;
	@Column
	private String category3;
	@Column
	private String category4;
	@Column
	private String category5;
	@Column
	private String intro;
	@Column
	private String fcmToken;
	@Column
	private String useYn;
	@Column
	private String userType;
	@Column
	private String email;
	@Column
	private Date regDate;
	@Column
	private String provider;
	@Column
	private String providerId;
	@Column
	private Integer activeScore;
	@Column
	private Integer userBadgeId;
	
	public UserDto toDto() {
		UserDto dto = UserDto.builder()
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
				.build();
		
		return dto;
	}
}
