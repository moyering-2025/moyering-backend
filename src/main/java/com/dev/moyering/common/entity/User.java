package com.dev.moyering.common.entity;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.dev.moyering.common.dto.UserDto;

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
	private String id;
	@Column
	private String profile;
	@Column
	private String password;
	@Column
	private String name;
	@Column
	private String nickname;
	@Column
	private String tel;
	@Column
	private Date birthday;
	@Column
	private String addr;
	@Column
	private String detailAddr;
	@Column
	private double latitude;
	@Column
	private double longitude;
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
	private String proviederId;
	@Column
	private Integer activeScore;
	@Column
	private Integer userBadgeId;
	
	public UserDto toDto() {
		UserDto dto = UserDto.builder()
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
		
		return dto;
	}
	 @Transient
	    public String getCategoryJsonString() {
		 if(category1==null&&category2==null&&category3==null&&category4==null&&category5==null) {
			 return null;
		 }
	        List<String> categories = new ArrayList<>();
	        if (category1 != null && !category1.isBlank()) categories.add("'" + category1 + "'");
	        if (category2 != null && !category2.isBlank()) categories.add("'" + category2 + "'");
	        if (category3 != null && !category3.isBlank()) categories.add("'" + category3 + "'");
	        if (category4 != null && !category4.isBlank()) categories.add("'" + category4 + "'");
	        if (category5 != null && !category5.isBlank()) categories.add("'" + category5 + "'");
	        return "[" + String.join(", ", categories) + "]";
	    }
}
