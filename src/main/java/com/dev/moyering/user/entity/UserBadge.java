package com.dev.moyering.user.entity;

import com.dev.moyering.admin.entity.AdminBadge;
import com.dev.moyering.user.dto.UserBadgeDto;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserBadge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userBadgeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "badge_id")
    private AdminBadge badge;

    @Column
    private Timestamp earnedAt;

    @Column
    private Boolean isRepresentative;

    @Column
    private String badge_img;

    public UserBadgeDto toDto(){
        UserBadgeDto dto = UserBadgeDto.builder()
                .userBadgeId(userBadgeId)
                .isRepresentative(isRepresentative)
                .badgeImg(badge.getBadgeImg()).build();

        return dto;
    }
}
