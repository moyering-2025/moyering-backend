package com.dev.moyering.gathering.dto;

import java.sql.Date;

import com.dev.moyering.gathering.entity.Gathering;
import com.dev.moyering.gathering.entity.GatheringLikes;
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
public class GatheringLikesDto {
	  private Integer gatheringLikeId;
	    private Integer gatheringId;
	    private Integer userId; // User 엔티티 대신 userId만 전달
	    private String nickName;
	    private String profile;
	    private String intro;
	    private Date likeDate;   

	    public GatheringLikes toEntity() {
	    	return GatheringLikes.builder()
	    			.gatheringLikeId(gatheringLikeId)
	    			.gathering(Gathering.builder().gatheringId(gatheringId).build())
	    			.user(User.builder().userId(userId).build())
	    			.likeDate(likeDate)
	    			.build();
	    }
}
