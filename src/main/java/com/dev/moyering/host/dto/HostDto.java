package com.dev.moyering.host.dto;

import javax.persistence.Column;

import com.dev.moyering.host.entity.Host;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HostDto {
	private Integer hostId;
	private Integer userId;
	private String name;
	private String profile;
	private String tel;
	private String publicTel;
	private String email;
	private String intro;
	private String tag1;
	private String tag2;
	private String tag3;
	private String tag4;
	private String tag5;
	private String bankName;
	private String accName;
	private String accNum;
	private String idCard;

	
	public Host toEntity() {
		Host entity = Host.builder()
				.hostId(hostId)
				.userId(userId)
				.name(name)
				.profile(profile)
				.tel(tel)
				.publicTel(publicTel)
				.email(email)
				.intro(intro)
				.tag1(tag1)
				.tag2(tag2)
				.tag3(tag3)
				.tag4(tag4)
				.tag5(tag5)
				.bankName(bankName)
				.accName(accName)
				.accNum(accNum)
				.idCard(idCard)
				.build();
		return entity;
	}
}
