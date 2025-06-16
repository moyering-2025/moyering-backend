package com.dev.moyering.dto.host;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.dev.moyering.entity.host.HostClass;
import com.dev.moyering.entity.host.Host;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HostClassDto {
	private Integer classId;
	private Integer hostId;
	private String category1;
	private String category2;
	private String name;
	private String locName;
	private String addr;
	private double latitude;
	private double longitude;
	private Integer recruitMin;
	private Integer recruitMax;
	private String img1;
	private String img2;
	private String img3;
	private String img4;
	private String img5;
	private String detailDescription;
	private String material;
	private String refund;
	
	public HostClass toEntity() {
		HostClass entity = HostClass.builder()
				.classId(classId)
				.category1(category1)
				.category2(category2)
				.name(name)
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
		if(hostId!=null) {
			entity.setHost(Host.builder()
					.hostId(hostId)
					.build());
		}
		return entity;
	}
}
