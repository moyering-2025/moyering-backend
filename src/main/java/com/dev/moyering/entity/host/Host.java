package com.dev.moyering.entity.host;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.dev.moyering.entity.common.User;

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
	@OneToOne
	@JoinColumn(name="userId")
	private User User;
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

}
