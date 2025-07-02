package com.dev.moyering.admin.entity;

import com.dev.moyering.admin.dto.AdminBadgeScoreDto;
import lombok.*;
import org.checkerframework.checker.units.qual.C;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA용 기본 생성자, 빈 객체 생성 차단
@Entity
@ToString
@Table(name = "badge_score")
public class AdminBadgeScore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer activeScoreId; // 점수 id

    @Column
    private String title; // 활동명

    @Column
    private Integer score; // 점수

    @Builder
    public AdminBadgeScore(Integer activeScoreId, String title, Integer score) {
        this.activeScoreId = activeScoreId;
        this.title = title;
        this.score = score;
    }

    // dto -> entity 변환
    public AdminBadgeScoreDto toDto(){
        return AdminBadgeScoreDto.builder()
                .activeScoreId(activeScoreId)
                .title(title)
                .score(score)
                .build();
    }
}
