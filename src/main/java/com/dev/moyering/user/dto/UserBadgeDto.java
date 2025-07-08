package com.dev.moyering.user.dto;

import java.sql.Timestamp;

import com.dev.moyering.admin.dto.AdminBadgeDto;
import com.dev.moyering.admin.entity.AdminBadge;
import com.dev.moyering.user.entity.User;
import com.dev.moyering.user.entity.UserBadge;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserBadgeDto {
    private Integer userBadgeId;

    private String badgeImg;

    private Boolean isRepresentative;

    private Integer userId;
    private Integer badgeId;
    private Timestamp earnedAt;

    public UserBadge toEntity() {
    	return UserBadge.builder()
    			.userBadgeId(userBadgeId)
    			.user(userId!=null? User.builder().userId(userId).build():null)
    			.badge(badgeId!=null? AdminBadge.builder().badgeId(badgeId).build():null)
    			.earnedAt(earnedAt)
    			.isRepresentative(isRepresentative)
    			.badge_img(badgeImg)
    			.build();
    			
    }
}
