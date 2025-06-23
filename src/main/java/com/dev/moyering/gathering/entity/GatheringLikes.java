package com.dev.moyering.gathering.entity;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;

import com.dev.moyering.common.entity.SubCategory;
import com.dev.moyering.gathering.dto.GatheringLikesDto;
import com.dev.moyering.user.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GatheringLikes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer gatheringLikeId;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="gatheringId")
	private Gathering gathering;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="userId")
	private User user;

    @Column()
	@CreationTimestamp
    private Date likeDate;
    
    public GatheringLikesDto toDto() {
	    return GatheringLikesDto.builder()
	    		.gatheringLikeId(gatheringLikeId)
	    		.gatheringId(gathering.getGatheringId())
	    		.userId(user.getUserId())
	    		.nickName(user.getNickName())
	    		.profile(user.getProfile())
	    		.intro(user.getIntro())
	    		.likeDate(likeDate)
	    		.build();
    }
}
