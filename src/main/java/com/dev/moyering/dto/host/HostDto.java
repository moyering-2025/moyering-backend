package com.dev.moyering.dto.host;

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
}
