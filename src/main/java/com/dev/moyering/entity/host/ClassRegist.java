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
public class ClassRegist {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer sudentId;
	@Column
	private Integer userId;
	@Column
	private Integer attCount;
	@Column
	private Integer calendarId;//다대다이기 때문에 다르게 처리해야함
}
