package com.dev.moyering.admin.service;

import com.dev.moyering.admin.dto.NoticeDto;
import com.dev.moyering.admin.entity.Notice;
import com.dev.moyering.admin.repository.NoticeRepository;
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
    public class NoticeServiceImpl implements NoticeService {
    private final NoticeRepository noticeRepository;

    /**
     * 공지사항 등록
     */
    @Override
    @Transactional
    public NoticeDto createNotice(NoticeDto noticeDto) {
        try {
            log.info("=== 공지사항 등록 시작 ===");
            log.info("제목: {}, 내용 : {},  핀 여부: {}, 등록일 : {}",
                    noticeDto.getTitle(), noticeDto.getContent(),
                    noticeDto.isPinYn(), noticeDto.getCreatedAt());

            // 1. DTO -> Entity 변환
            Notice notice = noticeDto.toEntity();
            log.debug("DTO -> Entity 변환 완료!");

            // 2. 영속화
            Notice savedNotice = noticeRepository.save(notice);
            log.info("공지사항 저장 완료 = ID = {}", savedNotice.getNoticeId());

            // 3. Entity -> DTO 변환 및 반환
            NoticeDto result = savedNotice.toDto();
            log.info("공지사항 등록 완료 : ID = {}", savedNotice.getNoticeId());
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
    public NoticeDto updateNotice(Integer noticeId, NoticeDto noticeDto) {
        log.info("공지사항 수정 시작 : noticeId ={}", noticeId);

        // 엔티티 조회 - orElseThrow 사용
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 공지사항을 찾을 수 없습니다. ID : " + noticeId));

        // 비즈니스 메서드 호출해서 값 수정
        notice.changeNotice(noticeDto.getTitle(), noticeDto.getContent());
        notice.changePinStatus(noticeDto.isPinYn());

        log.info("공지사항 수정 완료 : noticeId = {}", noticeId);
        return notice.toDto();
    }


    @Override
    @Transactional
    public void deleteNotice(Integer noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("공지사항이 존재하지 않습니다."));
        noticeRepository.delete(notice);
    }


    /**
     * 공지사항 목록 조회 (검색 포함)
     */

    @Override
    public Page<NoticeDto> getNoticeList(String searchKeyword, Pageable pageable) {
        log.info("=== 공지사항 목록 조회 시작 ===");
        log.info("검색어: {}, 페이지: {}", searchKeyword, pageable.getPageNumber());

        try {
            Page<NoticeDto> result;

            if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
                log.info("검색 모드");
                result = noticeRepository.findNoticesByKeyword(searchKeyword, pageable);
            } else {
                // 전체 조회
                log.info("전체 목록 조회");
                Page<Notice> noticePage = noticeRepository.findAll(pageable);
                result = noticePage.map(Notice::toDto); // Entity -> DTO 변환
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
    public NoticeDto getNoticeById(Integer noticeId) {
        log.info("공지사항 단건 조회: noticeId={}", noticeId);

        NoticeDto notice = noticeRepository.findNoticeByNoticeId(noticeId);
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
    public NoticeDto changePinStatus(Integer noticeId, boolean pinYn) {
        log.info("공지사항 핀 상태 변경: noticeId={}, pinYn={}", noticeId, pinYn);

        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 공지사항을 찾을 수 없습니다. ID: " + noticeId));

        notice.changePinStatus(pinYn);

        log.info("공지사항 핀 상태 변경 완료: noticeId={}", noticeId);
        return notice.toDto();
    }


    @Override
    @Transactional
    public void noticeVisibility(Integer noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("공지사항이 존재하지 않습니다."));

        notice.toggleVisibility(); // isHidden 값을 반대로 전환
        notice.toDto();
    }
    // 보이기, 숨기기 처리 상태
    @Override
    @Transactional
    public void hideNotice(Integer noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("공지사항이 없습니다."));
        notice.hide(); // isHidden = true
    }

    @Override
    @Transactional
    public void showNotice(Integer noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("공지사항이 없습니다."));
        notice.show(); // isHidden = false
    }

}
