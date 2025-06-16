package com.dev.moyering.entity.user;

import java.math.BigDecimal;
import java.sql.Date;
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

import com.dev.moyering.entity.common.SubCategory;

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
    @Column(name = "gathering_id", nullable = false)
    private Integer gatheringId;

    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "LONGTEXT", nullable = false)
    @Lob
    private String gatheringContent;

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

    @Column(nullable = false)
	@ColumnDefault("2")
    private Integer minAttendees;

    @Column()
    private Integer maxAttendees;

    @Column(nullable = false)
    private Date applyDeadline;

    @Column()
    private String preparationItems;

    @Column()
    private String tags;

    @Column(nullable = false)
    private Date createDate;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="subCategoryId")
	private SubCategory subCategory;

    @Column(precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column( precision = 10, scale = 7)
    private BigDecimal longitude;

    @Column()
    private String intrOnln;

    @Column()
    private String status;

    @Column(nullable = false)
    private String thumbnail;
}