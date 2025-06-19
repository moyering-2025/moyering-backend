package com.dev.moyering.gathering.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;

import com.dev.moyering.gathering.dto.GatheringApplyDto;
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
public class GatheringApply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer gatheringApplyId;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="gatheringId")
	private Gathering gathering;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="userId")
	private User user;
	
	@Column()
	private String aspiration;

    @Column()
	@CreationTimestamp
    private Date applyDate;

    @Column(columnDefinition = "TINYINT")
    private Boolean isApproved;
    
    public GatheringApplyDto toDto() {
	    return GatheringApplyDto.builder()
	    		.gatheringApplyId(gatheringApplyId)
	    		.gatheringId(gathering.getGatheringId())
	    		.userId(user.getUserId())
	    		.nickName(user.getNickName())
	    		.intro(user.getIntro())
	    		.profile(user.getProfile())
	    		.aspiration(aspiration)
	    		.applyDate(applyDate)
	    		.isApprove(isApproved)
	    		.build();
    }
}