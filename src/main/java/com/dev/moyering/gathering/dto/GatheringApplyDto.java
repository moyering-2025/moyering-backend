package com.dev.moyering.gathering.dto;

import java.sql.Date;
import java.sql.Timestamp;

import com.dev.moyering.gathering.entity.Gathering;
import com.dev.moyering.gathering.entity.GatheringApply;
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
public class GatheringApplyDto {

    private Integer gatheringApplyId;
    
    private Integer gatheringId;
    private String title;
    private String thumbnailFileName;
    private Date meetingDate;
    @JsonFormat(pattern = "HH:mm")
    private String startTime;
    @JsonFormat(pattern = "HH:mm")
    private String endTime;
    private String address;
    private String locName;
    private Integer minAttendees;
    private Integer maxAttendees;
    
    private Timestamp applyDeadline;
//    private String preparationItems;
    private String tags;
    private String intrOnln;
    private Boolean canceled;
    
    private Integer userId; // User 엔티티 대신 userId만 전달
    private String nickName;
    private String profile;
    private String intro;
    private Date applyDate;   
    private Boolean isApprove;
    private String aspiration;
    
    public GatheringApply toEntity() {
    	return GatheringApply.builder()
    			.gatheringApplyId(gatheringApplyId)
    			.gathering(Gathering.builder().gatheringId(gatheringId).build())
    			.user(User.builder().userId(userId).build())
    			.applyDate(applyDate)
    			.isApproved(isApprove)
    			.aspiration(aspiration)
    			.build();
    }
}