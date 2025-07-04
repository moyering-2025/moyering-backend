package com.dev.moyering.admin.service;

import com.dev.moyering.admin.dto.AdminBadgeDto;
import com.dev.moyering.admin.dto.AdminBadgeScoreDto;
import com.dev.moyering.admin.entity.AdminBadge;
import com.dev.moyering.admin.entity.AdminBadgeScore;
import com.dev.moyering.admin.repository.AdminBadgeRepository;
import com.dev.moyering.admin.repository.AdminBadgeScoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminBadgeScoreServiceImpl implements AdminBadgeScoreService {
    private final AdminBadgeScoreRepository adminBadgeScoreRepository;
    private final AdminBadgeRepository adminBadgeRepository;

    // List<Badge> → stream() → map(변환) → collect(수집) → List<BadgeDto>
    @Override
    public List<AdminBadgeDto> getAllBadges() {
        List<AdminBadge> badges = adminBadgeRepository.findAll(); // 리파지토리에서 모든 배지 엔티티 가져오기
        return badges.stream() // 각 엔티티의 toDto()메서드 사용해서 dto리스트로 변환
                .map(AdminBadge::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AdminBadgeScoreDto> getAllBadgeScores() {
        List<AdminBadgeScore> badgeScore = adminBadgeScoreRepository.findAll();
        return badgeScore.stream()
                .map(AdminBadgeScore::toDto)
                .collect(Collectors.toList());
    }


    @Transactional // 배지 점수 수정
    @Override
    public AdminBadgeScoreDto updateBadgeScores(AdminBadgeScoreDto adminBadgeScoreDto) {
        // 리파지토리에서 엔티티 리스트 조회
        List<AdminBadgeScore> badgeScores = adminBadgeScoreRepository.findAll();

        // 수정하려는 활동점수가 실제로 있는지 확인
        AdminBadgeScore existingScore = adminBadgeScoreRepository
                .findById(adminBadgeScoreDto.getActiveScoreId())
                .orElseThrow(() -> new RuntimeException("해당 활동점수를 찾을 수 없습니다. ID : " + adminBadgeScoreDto.getActiveScoreId()));
        log.info("기존 데이터 : ID = [}, title = {}, score = {}", existingScore.getActiveScoreId(), existingScore.getTitle(), existingScore.getScore());

        // 기존 엔티티 값을 새로운 값으로 업데이트

        AdminBadgeScore updatedScore = AdminBadgeScore.builder()
                .activeScoreId(existingScore.getActiveScoreId()) // ID는 그대로
                .title(adminBadgeScoreDto.getTitle() != null ?
                        adminBadgeScoreDto.getTitle() : existingScore.getTitle())
                .score(adminBadgeScoreDto.getScore() != null ?
                        adminBadgeScoreDto.getScore() : existingScore.getScore())
                .build();

        log.info("업데이트할 데이터: ID={}, title={}, score={}",
                updatedScore.getActiveScoreId(), updatedScore.getTitle(), updatedScore.getScore());
        // 데이터 베이스에 저장
        AdminBadgeScore savedScore = adminBadgeScoreRepository.save(updatedScore);

        log.info("자징 데이터: ID={}, title={}, score={}",
                savedScore.getActiveScoreId(), savedScore.getTitle(), savedScore.getScore());
        return savedScore.toDto();
    }
}