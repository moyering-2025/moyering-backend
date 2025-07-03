package com.dev.moyering.admin.dto;

import com.dev.moyering.admin.entity.AdminBadgeScore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminBadgeScoreDto {
    private Integer activeScoreId; // 점수 id
    private String title; // 활동명
    private Integer score; // 점수

    @Builder
    public AdminBadgeScoreDto(Integer activeScoreId, String title, Integer score) {
        this.activeScoreId = activeScoreId;
        this.title = title;
        this.score = score;
    }

    // dto -> entity 변환
    public AdminBadgeScore toEntity() {
        return AdminBadgeScore.builder()
                .activeScoreId(activeScoreId)
                .title(title)
                .score(score)
                .build();

    }
}
