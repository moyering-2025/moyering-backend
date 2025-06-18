package com.dev.moyering.gathering.dto;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalTime;

import com.dev.moyering.common.entity.SubCategory;
import com.dev.moyering.common.entity.User;
import com.dev.moyering.gathering.entity.Gathering;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GatheringDto {

    private Integer gatheringId;
    private String title;
    private Integer userId;// User 엔티티 대신 userId만 전달
    
    private String name;
    private String profile;
    private String intro;
    private String categorys;
    
    private String gatheringContent;
    private String thumbnailFileName;
    private Date meetingDate;
    private String startTime;
    private String endTime;
    private String address;
    private String detailAddress;
    private String locName;
    private Integer minAttendees;
    private Integer maxAttendees;
    private Date applyDeadline;
    private String preparationItems;
    private String tags;
    private Date createDate;    
    private Integer categoryId; // SubCategory 엔티티 대신 ID만 전달
    private Integer subCategoryId; // SubCategory 엔티티 대신 ID만 전달
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String intrOnln;
    private String status;
    
    public Gathering toEntity() {
    	return Gathering.builder()
    			.gatheringId(gatheringId)
    			.title(title)
    			.user(User.builder().userId(userId).build())
    			.gatheringContent(gatheringContent)
    			.thumbnail(thumbnailFileName)
    			.meetingDate(meetingDate)
    			.startTime(startTime != null ? LocalTime.parse(startTime) : null)
    			.endTime(endTime != null ? LocalTime.parse(endTime) : null)
    			.address(address)
    			.detailAddress(detailAddress)
    			.locName(locName)
    			.minAttendees(minAttendees)
    			.maxAttendees(maxAttendees)
    			.applyDeadline(applyDeadline)
    			.preparationItems(preparationItems)
    			.tags(tags)
    			.createDate(createDate)
    			.subCategory(SubCategory.builder().subCategoryId(subCategoryId).build())
    			.latitude(latitude)
    			.longitude(longitude)
    			.status(status)
    			.build();
    }
}