package com.dev.moyering.entity.host;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.dev.moyering.dto.host.ClassDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Class {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer classId;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="hostId")
	private Host host;
	@Column
	private String category1;
	@Column
	private String category2;
	@Column
	private String name;
	@Column
	private String locName;
	@Column
	private String addr;
	@Column
	private double latitude;
	@Column
	private double longitude;
	@Column
	private Integer recruitMin;
	@Column
	private Integer recruitMax;
	@Column
	private String img1;
	@Column
	private String img2;
	@Column
	private String img3;
	@Column
	private String img4;
	@Column
	private String img5;
	@Column
	private String detailDescription;
	@Column
	private String material;
	@Column
	private String refund;
	
	public ClassDto toDto() {
		ClassDto dto = ClassDto.builder()
				.classId(classId)
				.category1(category1)
				.category2(category2)
				.name(locName)
				.locName(locName)
				.addr(addr)
				.latitude(latitude)
				.longitude(longitude)
				.recruitMin(recruitMin)
				.recruitMax(recruitMax)
				.img1(img1)
				.img2(img2)
				.img3(img3)
				.img4(img4)
				.img5(img5)
				.detailDescription(detailDescription)
				.material(material)
				.refund(refund)
				.build();
		if(host!=null) {
			dto.setHostId(host.getHostId());
		}
		return dto;
	}

}
