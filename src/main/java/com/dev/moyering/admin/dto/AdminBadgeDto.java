package com.dev.moyering.admin.dto;

import com.dev.moyering.admin.entity.AdminBadge;
import lombok.Builder;
import lombok.Getter;

import java.sql.Date;

@Builder
@Getter
public class AdminBadgeDto {
    private Integer badgeId; // 배지 아이디
    private String badgeImg; // 배지 이미지
    private String badgeName; // 배지 이름
    private String badgeContent; // 배지 설명
    private Integer cumulScore; // 누적점수
    private Date createdAt; //생성시간

    @Builder
    public AdminBadgeDto(Integer badgeId, String badgeImg, String badgeName, String badgeContent, Integer cumulScore, Date createdAt) {
        this.badgeId = badgeId;
        this.badgeImg = badgeImg;
        this.badgeName = badgeName;
        this.badgeContent = badgeContent;
        this.cumulScore = cumulScore;
        this.createdAt = createdAt;
    }

    public AdminBadge toEntity() {
        return AdminBadge.builder()
                .badgeId(badgeId)
                .badgeImg(badgeImg)
                .badgeName(badgeName)
                .badgeContent(badgeContent)
                .cumulScore(cumulScore)
                .createdAt(createdAt)
                .build();
    }
}
