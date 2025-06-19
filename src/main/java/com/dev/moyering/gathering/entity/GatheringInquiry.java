package com.dev.moyering.gathering.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;

import com.dev.moyering.gathering.dto.GatheringInquiryDto;
import com.dev.moyering.user.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class GatheringInquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer inquiryId;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="gatheringId")
	private Gathering gathering;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="userId")
	private User user;
	
	@Column(nullable = false)
    private String inquiryContent;
	
	@Column
	@CreationTimestamp
	private Date inquiryDate;
	
	@Column
	private Date responseDate;
	
	@Column
	private String responseContent;

    public GatheringInquiryDto toDto() {
    	GatheringInquiryDto.GatheringInquiryDtoBuilder builder = GatheringInquiryDto.builder()
   			 .inquiryContent(inquiryContent)
   			 .gatheringId(gathering.getGatheringId())
   			 .title(gathering.getTitle())
   			 .nickName(user.getNickName())
   			 .profile(user.getProfile())
   			 .inquiryDate(inquiryDate);
	    if (responseDate != null && responseContent != null) {
	        builder.responseDate(responseDate);
	    	builder.responseContent(responseContent);
	    	builder.responseState("답변완료");
	    } else {
	    	builder.responseState("답변대기");
	    }
   		    return builder.build();
    }
}
