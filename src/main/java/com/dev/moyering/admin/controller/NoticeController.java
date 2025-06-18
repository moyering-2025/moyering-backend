// ===== 1. 수정된 NoticeController.java =====
package com.dev.moyering.admin.controller;

import com.dev.moyering.admin.dto.NoticeDto;
import com.dev.moyering.admin.service.NoticeServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notice")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class NoticeController {

    private final NoticeServiceImpl noticeService;

    /**
     * 공지사항 등록
     * POST /api/notices
     */
    @PostMapping
    public ResponseEntity<NoticeDto> createNotice(@RequestBody NoticeDto noticeDto) {
        log.info("공지사항 등록 요청: {}", noticeDto.getTitle());

        try {
            NoticeDto createdNotice = noticeService.createNotice(noticeDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdNotice);
        } catch (Exception e) {
            log.error("공지사항 등록 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 공지사항 수정
     * PUT /api/notices/{noticeId}
     */
    @PutMapping("/{noticeId}")
    public ResponseEntity<NoticeDto> updateNotice(
            @PathVariable Integer noticeId,
            @RequestBody NoticeDto noticeDto) {

        log.info("공지사항 수정 요청: noticeId={}", noticeId);

        try {
            NoticeDto updatedNotice = noticeService.updateNotice(noticeId, noticeDto);
            return ResponseEntity.ok(updatedNotice);
        } catch (IllegalArgumentException e) {
            log.error("공지사항 수정 실패 - 찾을 수 없음: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("공지사항 수정 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 공지사항 삭제 (숨기기/보이기 토글)
     * DELETE /api/notices/{noticeId}
     */
    @DeleteMapping("/{noticeId}")
    public ResponseEntity<Void> deleteNotice(@PathVariable Integer noticeId) {
        log.info("공지사항 삭제 요청: {}", noticeId);
        try {
            noticeService.deleteNotice(noticeId);
            return ResponseEntity.noContent().build(); // 204
        } catch (IllegalArgumentException e) {
            log.error("공지사항 삭제 실패 - 존재하지 않음: {}", e.getMessage());
            return ResponseEntity.notFound().build(); // 404
        } catch (Exception e) {
            log.error("공지사항 삭제 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500
        }
    }


    @PatchMapping("/{noticeId}/visibility")
    public ResponseEntity<Void> toggleNoticeVisibility(@PathVariable Integer noticeId) {
        log.info("공지사항 숨김/게시 토글 요청: noticeId = {}", noticeId);
        try {
            noticeService.noticeVisibility(noticeId);
            return ResponseEntity.noContent().build(); // 204
        } catch (IllegalArgumentException e) {
            log.error("공지사항 숨김/게시 실패 - 존재하지 않음: {}", e.getMessage());
            return ResponseEntity.notFound().build(); // 404
        } catch (Exception e) {
            log.error("공지사항 숨김/게시 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500
        }
    }

    // 숨기기
    @PatchMapping("/{noticeId}/hide")
    public ResponseEntity<Void> hideNotice(@PathVariable Integer noticeId) {
        try {
            noticeService.hideNotice(noticeId);
            log.info(">>> 공지사항 숨기기 요청: {}", noticeId);
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 보이기
    @PatchMapping("/{noticeId}/show")
    public ResponseEntity<Void> showNotice(@PathVariable Integer noticeId) {
        try {
            noticeService.showNotice(noticeId);
            log.info(">>> 공지사항 보이기 요청: {}", noticeId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



    /**
     * 공지사항 목록 조회 (검색 + 페이징)
     * GET /api/notices
     */
    @GetMapping
    public ResponseEntity<Page<NoticeDto>> getNoticeList(
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        log.info("공지사항 목록 조회 요청: keyword={}, page={}", keyword, pageable.getPageNumber());

        try {
            Page<NoticeDto> notices = noticeService.getNoticeList(keyword, pageable);
            return ResponseEntity.ok(notices);
        } catch (Exception e) {
            log.error("공지사항 목록 조회 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 공지사항 핀 상태 변경
     * PATCH /api/notices/{noticeId}/pin
     */
    @PatchMapping("/{noticeId}/pin")
    public ResponseEntity<NoticeDto> changePinStatus(
            @PathVariable Integer noticeId,
            @RequestParam boolean pinYn) {

        log.info("공지사항 핀 상태 변경 요청: noticeId={}, pinYn={}", noticeId, pinYn);

        try {
            NoticeDto updatedNotice = noticeService.changePinStatus(noticeId, pinYn);
            return ResponseEntity.ok(updatedNotice);
        } catch (IllegalArgumentException e) {
            log.error("핀 상태 변경 실패 - 찾을 수 없음: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("핀 상태 변경 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 공지사항 단건 조회
     * GET /api/notices/{noticeId}
     */
    @GetMapping("/{noticeId}")
    public ResponseEntity<NoticeDto> getNotice(@PathVariable Integer noticeId) {
        log.info("공지사항 단건 조회 요청: noticeId={}", noticeId);

        try {
            NoticeDto notice = noticeService.getNoticeById(noticeId);
            return ResponseEntity.ok(notice);
        } catch (IllegalArgumentException e) {
            log.error("공지사항 조회 실패 - 찾을 수 없음: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("공지사항 조회 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

}