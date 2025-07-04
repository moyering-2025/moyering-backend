package com.dev.moyering.user.dto;

import com.dev.moyering.admin.dto.AdminBadgeDto;
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

}
