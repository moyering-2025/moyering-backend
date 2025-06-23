package com.dev.moyering.host.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.dev.moyering.host.dto.HostDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Host {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer hostId;
	@Column
	private Integer userId;
	@Column
	private String name;
	@Column
	private String profile;
	@Column
	private String tel;
	@Column
	private String publicTel;
	@Column
	private String email;
	@Column
	private String intro;
	@Column
	private String tag1;
	@Column
	private String tag2;
	@Column
	private String tag3;
	@Column
	private String tag4;
	@Column
	private String tag5;
	@Column
	private String bankName;
	@Column
	private String accName;
	@Column
	private String accNum;
	@Column
	private String idCard;
	
	public HostDto toDto() {
		HostDto dto = HostDto.builder()
				.hostId(hostId)
				.userId(userId)
				.name(accName)
				.profile(profile)
				.tel(publicTel)
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
		return dto;
				
	}

}
