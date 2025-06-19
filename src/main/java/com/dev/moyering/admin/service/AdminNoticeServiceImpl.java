package com.dev.moyering.admin.service;

import com.dev.moyering.admin.dto.AdminNoticeDto;
import com.dev.moyering.admin.entity.AdminNotice;
import com.dev.moyering.admin.repository.AdminNoticeRepository;
import com.dev.moyering.admin.repository.AdminNoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
    public class AdminNoticeServiceImpl implements AdminNoticeService {
    private final AdminNoticeRepository adminNoticeRepository;

    /**
     * 공지사항 등록
     */
    @Override
    @Transactional
    public AdminNoticeDto createNotice(AdminNoticeDto noticeDto) {
        try {
            log.info("=== 공지사항 등록 시작 ===");
            log.info("제목: {}, 내용 : {},  핀 여부: {}, 등록일 : {}",
                    noticeDto.getTitle(), noticeDto.getContent(),
                    noticeDto.isPinYn(), noticeDto.getCreatedAt());

            // 1. DTO -> Entity 변환
            AdminNotice adminNotice = noticeDto.toEntity();
            log.debug("DTO -> Entity 변환 완료!");

            // 2. 영속화
            AdminNotice savedAdminNotice = adminNoticeRepository.save(adminNotice);
            log.info("공지사항 저장 완료 = ID = {}", savedAdminNotice.getNoticeId());

            // 3. Entity -> DTO 변환 및 반환
            AdminNoticeDto result = savedAdminNotice.toDto();
            log.info("공지사항 등록 완료 : ID = {}", savedAdminNotice.getNoticeId());
            return result;

        } catch (Exception e) {
            log.error("공지사항 등록 실패 : {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 공지사항 수정
     */
    @Override
    @Transactional
    public AdminNoticeDto updateNotice(Integer noticeId, AdminNoticeDto noticeDto) {
        log.info("공지사항 수정 시작 : noticeId ={}", noticeId);

        // 엔티티 조회 - orElseThrow 사용
        AdminNotice adminNotice = adminNoticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 공지사항을 찾을 수 없습니다. ID : " + noticeId));

        // 비즈니스 메서드 호출해서 값 수정
        adminNotice.changeNotice(noticeDto.getTitle(), noticeDto.getContent());
        adminNotice.changePinStatus(noticeDto.isPinYn());

        log.info("공지사항 수정 완료 : noticeId = {}", noticeId);
        return adminNotice.toDto();
    }


    @Override
    @Transactional
    public void deleteNotice(Integer noticeId) {
        AdminNotice adminNotice = adminNoticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("공지사항이 존재하지 않습니다."));
        adminNoticeRepository.delete(adminNotice);
    }


    /**
     * 공지사항 목록 조회 (검색 포함)
     */

    @Override
    public Page<AdminNoticeDto> getNoticeList(String searchKeyword, Pageable pageable) {
        log.info("=== 공지사항 목록 조회 시작 ===");
        log.info("검색어: {}, 페이지: {}", searchKeyword, pageable.getPageNumber());

        try {
            Page<AdminNoticeDto> result;

            if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
                log.info("검색 모드");
                result = adminNoticeRepository.findNoticesByKeyword(searchKeyword, pageable);
            } else {
                // 전체 조회
                log.info("전체 목록 조회");
                Page<AdminNotice> noticePage = adminNoticeRepository.findAll(pageable);
                result = noticePage.map(AdminNotice::toDto); // Entity -> DTO 변환
            }

            log.info("조회 완료: 총 {}건", result.getTotalElements());
            return result;

        } catch (Exception e) {
            log.error("목록 조회 실패: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 공지사항 단건 조회
     */
    @Override
    public AdminNoticeDto getNoticeById(Integer noticeId) {
        log.info("공지사항 단건 조회: noticeId={}", noticeId);

        AdminNoticeDto notice = adminNoticeRepository.findNoticeByNoticeId(noticeId);
        if (notice == null) {
            throw new IllegalArgumentException("해당 공지사항을 찾을 수 없습니다. ID: " + noticeId);
        }

        return notice;
    }


    /**
     * 핀 상태 변경
     */
    @Override
    @Transactional
    public AdminNoticeDto changePinStatus(Integer noticeId, boolean pinYn) {
        log.info("공지사항 핀 상태 변경: noticeId={}, pinYn={}", noticeId, pinYn);

        AdminNotice adminNotice = adminNoticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 공지사항을 찾을 수 없습니다. ID: " + noticeId));

        adminNotice.changePinStatus(pinYn);

        log.info("공지사항 핀 상태 변경 완료: noticeId={}", noticeId);
        return adminNotice.toDto();
    }


    // 보이기, 숨기기 처리 상태
    @Override
    @Transactional
    public void hideNotice(Integer noticeId) {
        AdminNotice adminNotice = adminNoticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("공지사항이 없습니다."));
        adminNotice.hide(); // isHidden = true
        // 더티체킹 보장
        adminNoticeRepository.save(adminNotice);
        // 로그로 확인
        log.info(">>> 공지사항 숨김 처리 완료: noticeId = {}", noticeId);
    }

    @Override
    @Transactional
    public void showNotice(Integer noticeId) {
        AdminNotice adminNotice = adminNoticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("공지사항이 없습니다."));
        adminNotice.show(); // isHidden = false
        adminNoticeRepository.save(adminNotice);
        log.info (">>> 공지사항 표시 처리 완료 : noticeId = {}", noticeId);
    }

}
