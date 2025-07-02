package com.dev.moyering.user.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyRegisteredClassDto {
	private Integer calendarId;
	private Integer classId;
	private Date startDate;
	private Date endDate;
	private String status;
	private Integer registeredCount;
	
    private String name;
    private String locName;
    private String addr;
    private String detailAddr;
    private Double latitude;
    private Double longitude;
    private Integer recruitMin;
    private Integer recruitMax;
    private String imgName1;
    private String detailDescription;
    private String materialName;
    private String caution;
    private String incluision;
    private String preparation;
    private String keywords;
    private Integer price;
    private String category1;
    private String category2;

}
