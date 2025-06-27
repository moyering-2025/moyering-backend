package com.dev.moyering.gathering.dto;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.dev.moyering.common.entity.SubCategory;
import com.dev.moyering.gathering.entity.Gathering;
import com.dev.moyering.user.entity.User;
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
    private String nickName;
    private String profile;
    private String intro;
    
    private String gatheringContent;
    private String thumbnailFileName;
    private Date meetingDate;

    @JsonFormat(pattern = "HH:mm")
    private String startTime;
    
    @JsonFormat(pattern = "HH:mm")
    private String endTime;
    private String address;
    private String detailAddress;
    private String locName;
    private Integer minAttendees;
    private Integer maxAttendees;
    private Timestamp applyDeadline;
    private String preparationItems;
    private String tags;
    private Date createDate;    
    private Integer categoryId; // SubCategory 엔티티 대신 ID만 전달
    private Integer subCategoryId; // SubCategory 엔티티 대신 ID만 전달
    private String categoryName;
    private String subCategoryName;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String intrOnln;
    private String status;
    private Boolean canceled; 
    
    public Gathering toEntity() {
   	 Gathering.GatheringBuilder builder = Gathering.builder()
    			.gatheringId(gatheringId)
    			.title(title)
    			.user(User.builder().userId(userId).build())
    			.gatheringContent(gatheringContent)
    			.thumbnail(thumbnailFileName)
    			.meetingDate(meetingDate)
    			.startTime(LocalTime.parse(startTime, DateTimeFormatter.ofPattern("HH:mm")))
    			.endTime(LocalTime.parse(endTime, DateTimeFormatter.ofPattern("HH:mm")))
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
    			.longitude(longitude);
			   	 if(canceled || status.equals("취소됨")) {
			   		builder.canceled(true);
			   	 } else {
			   		 builder.canceled(false);
			   	 }
    		    return builder.build();
    }
    
}