package com.dev.moyering.admin.entity;

import com.dev.moyering.admin.dto.AdminBadgeDto;
import lombok.*;

import javax.persistence.*;
import java.sql.Date;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA용 기본 생성자, 빈 객체 생성 차단
@Entity
@ToString
@Table(name = "badge")
public class AdminBadge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer badgeId; // 배지 아이디

    @Column
    private String badgeImg; // 배지 이미지

    @Column
    private String badgeName; // 배지 이름

    @Column
    private String badgeContent; // 배지 설명

    @Column
    private Integer cumulScore; // 누적점수

    @Column
    private Date createdAt; //생성시간

    @Builder
    public AdminBadge(Integer badgeId, String badgeImg, String badgeName, String badgeContent, Integer cumulScore, Date createdAt) {
        this.badgeId = badgeId;
        this.badgeImg = badgeImg;
        this.badgeName = badgeName;
        this.badgeContent = badgeContent;
        this.cumulScore = cumulScore;
        this.createdAt = createdAt;
    }

    public AdminBadgeDto toDto() {
        return AdminBadgeDto.builder()
                .badgeId(badgeId)
                .badgeName(badgeName)
                .badgeImg(badgeImg)
                .badgeContent(badgeContent)
                .cumulScore(cumulScore)
                .createdAt(createdAt)
                .build();
    }
}

