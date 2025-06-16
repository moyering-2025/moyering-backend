package com.dev.moyering.dto.user;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalTime;

import com.dev.moyering.entity.common.SubCategory;
import com.dev.moyering.entity.common.User;
import com.dev.moyering.entity.user.Gathering;

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
    private Integer userId; // User 엔티티 대신 userId만 전달
    private String gatheringContent;
    private String thumbnail;
    private Date meetingDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String address;
    private String detailAddress;
    private Integer minAttendees;
    private Integer maxAttendees;
    private Date applyDeadline;
    private String preparationItems;
    private String tags;
    private Date createDate;
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
    			.thumbnail(thumbnail)
    			.meetingDate(meetingDate)
    			.startTime(startTime)
    			.endTime(endTime)
    			.address(address)
    			.detailAddress(detailAddress)
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