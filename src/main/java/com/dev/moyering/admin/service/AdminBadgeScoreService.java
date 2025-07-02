package com.dev.moyering.admin.service;

import com.dev.moyering.admin.dto.AdminBadgeDto;
import com.dev.moyering.admin.dto.AdminBadgeScoreDto;
import com.dev.moyering.admin.entity.AdminBadge;
import com.dev.moyering.admin.entity.AdminBadgeScore;

import java.util.List;

public interface AdminBadgeScoreService {
    // 서비스에서 직접 엔티티 노출하면 안된다.
    List<AdminBadgeDto> getAllBadges();
    List<AdminBadgeScoreDto> getAllBadgeScores();
    AdminBadgeScoreDto updateBadgeScores(AdminBadgeScoreDto adminBadgeScoreDto); // 수정 후, 클라이언트가 확인할 수 있는 반환값 AdminBadgeScoreDto

}
