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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import com.dev.moyering.gathering.dto.GatheringDto;
import com.dev.moyering.user.entity.User;
import com.dev.moyering.common.entity.SubCategory;

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
public class Gathering {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer gatheringId;

    @Column(nullable = false)
    private String title;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="userId")
	private User user;

    @Column(columnDefinition = "LONGTEXT", nullable = false)
    @Lob
    private String gatheringContent;

    @Column(nullable = false)
    private String thumbnail;
    
    @Column(nullable = false)
    private Date meetingDate;

    @Column(nullable = false)
    private LocalTime startTime;
    
    @Column()
    private LocalTime endTime;

    @Column()
    private String address;

    @Column()
    private String detailAddress;
    
    @Column()
    private String locName;

    @Column(nullable = false)
	@ColumnDefault("2")
    private Integer minAttendees;

    @Column()
    private Integer maxAttendees;

    @Column(nullable = false)
    private Timestamp applyDeadline;

    @Column()
    private String preparationItems;

    @Column()
    private String tags;

    @Column
	@CreationTimestamp
    private Date createDate;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="subCategoryId")
	private SubCategory subCategory;
	
    @Column(precision = 10, scale = 7)
    private BigDecimal latitude;//위도

    @Column( precision = 10, scale = 7)
    private BigDecimal longitude;//경도

    @Column()
    private String intrOnln;//한줄 소개

    @Column(columnDefinition = "TINYINT")
	@ColumnDefault("0")
    private Boolean canceled; //true : 취소됨 / false : 취소 안 됨. 
    
    public GatheringDto toDto() {
    	 GatheringDto.GatheringDtoBuilder builder = GatheringDto.builder()
    		        .gatheringId(gatheringId)
    		        .title(title)
    		        .userId(user.getUserId())
    		        .nickName(user.getNickName())
    		        .profile(user.getProfile())
    		        .intro(user.getIntro())
    		        .gatheringContent(gatheringContent)
    		        .thumbnailFileName(thumbnail)
    		        .meetingDate(meetingDate)
    		        .startTime(startTime.toString())
    		        .endTime(endTime.toString())
    		        .address(address)
    		        .detailAddress(detailAddress)
    		        .locName(locName)
    		        .minAttendees(minAttendees)
    		        .maxAttendees(maxAttendees)
    		        .applyDeadline(applyDeadline)
    		        .preparationItems(preparationItems)
    		        .tags(tags)
    		        .createDate(createDate)
    		        .subCategoryId(subCategory.getSubCategoryId())
    		        .subCategoryName(subCategory.getSubCategoryName())
    		        .latitude(latitude)
    		        .longitude(longitude)
    		        .intrOnln(intrOnln)
    		        .canceled(canceled);
    	 if(subCategory.getFirstCategory()!=null) {
    		 builder.categoryId(subCategory.getFirstCategory().getCategoryId());
    		 builder.categoryName(subCategory.getFirstCategory().getCategoryName());
    	 }
    	 if(canceled) {
    		 builder.status("취소됨");
    	 } else {
    		 builder.status("취소되지 않음");
    	 }
	    return builder.build();
    }
}