package com.dev.moyering.gathering.dto;

import java.sql.Date;


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
	// QueryDSL용 생성자 추가
    public GatheringInquiryDto(Integer inquiryId, Integer gatheringId, String title, 
                              Integer userId, String nickName, String profile,
                              String inquiryContent, Date inquiryDate, Date responseDate,
                              Date meetingDate, String responseContent) {
        this.inquiryId = inquiryId;
        this.gatheringId = gatheringId;
        this.title = title;
        this.userId = userId;
        this.nickName = nickName;
        this.profile = profile;
        this.inquiryContent = inquiryContent;
        this.inquiryDate = inquiryDate;
        this.responseDate = responseDate;
        this.meetingDate = meetingDate;
        this.responseContent = responseContent;
        
        // 응답 상태 설정
        if (responseDate != null && responseContent != null) {
            this.responseState = "답변완료";
        } else {
            this.responseState = "답변대기";
        }
    }
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