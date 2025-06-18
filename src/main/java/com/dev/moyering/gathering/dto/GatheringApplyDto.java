package com.dev.moyering.gathering.dto;

import java.sql.Date;

import com.dev.moyering.gathering.entity.Gathering;
import com.dev.moyering.gathering.entity.GatheringApply;
import com.dev.moyering.user.entity.User;

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
    private Integer userId; // User 엔티티 대신 userId만 전달
    private String name;
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