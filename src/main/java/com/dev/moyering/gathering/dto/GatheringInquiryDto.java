package com.dev.moyering.gathering.dto;

import java.util.Date;


import com.dev.moyering.gathering.entity.Gathering;
import com.dev.moyering.gathering.entity.GatheringInquiry;
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
public class GatheringInquiryDto {

	private Integer inquiryId;
	
    private Integer gatheringId;
    private String title;
    
    private Integer userId;// User 엔티티 대신 userId만 전달    
    private String nickName;
    private String profile;
    
    private String inquiryContent;
	private Date inquiryDate;
	private Date responseDate;
	private Date meetingDate;
	private String responseContent;
	private String responseState;
    
    public GatheringInquiry toEntity() {
	    GatheringInquiry.GatheringInquiryBuilder builder = GatheringInquiry.builder()
	    		.inquiryId(inquiryId)
				.gathering(Gathering.builder().gatheringId(gatheringId).build())
				.user(User.builder().userId(userId).build())
				.inquiryContent(inquiryContent)
				.inquiryDate(inquiryDate);
			    if (responseDate != null && responseContent != null) {
			        builder.responseDate(responseDate);
			    	builder.responseContent(responseContent);
			    }
    	return builder.build();
    }
}