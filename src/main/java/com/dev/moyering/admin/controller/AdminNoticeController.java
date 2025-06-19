package com.dev.moyering.admin.controller;
import com.dev.moyering.admin.dto.AdminNoticeDto;
import com.dev.moyering.admin.service.AdminNoticeServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/notice")
@RequiredArgsConstructor
@Slf4j // 로깅
@CrossOrigin(origins = "*")
public class AdminNoticeController {

    private final AdminNoticeServiceImpl noticeService;

    /**
     * 공지사항 등록
     */
    @PostMapping
    public ResponseEntity<AdminNoticeDto> createNotice(@RequestBody AdminNoticeDto noticeDto) {
        log.info("공지사항 등록 요청: {}", noticeDto.getTitle());

        try {
            AdminNoticeDto createdNotice = noticeService.createNotice(noticeDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdNotice);
        } catch (Exception e) {
            log.error("공지사항 등록 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 공지사항 수정
     */
    @PutMapping("/{noticeId}")
    public ResponseEntity<AdminNoticeDto> updateNotice(
            @PathVariable Integer noticeId,
            @Valid @RequestBody AdminNoticeDto noticeDto) { // 유효성 검증

        log.info("공지사항 수정 요청: noticeId={}", noticeId);

         // 공지사항 id 검증
        if (noticeDto.getNoticeId() != null && !noticeDto.getNoticeId().equals(noticeId)){
            log.warn("요청 ID불일치 : pathId = {}, bodyId = {}" , noticeId, noticeDto.getNoticeId());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

         // 검증 후 정상로직 수행

        try {
            AdminNoticeDto updatedNotice = noticeService.updateNotice(noticeId, noticeDto);
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
     * 공지사항 삭제
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




    /**
     * 공지사항 숨가가
     */
    @PatchMapping("/{noticeId}/hide")
    public ResponseEntity<AdminNoticeDto> hideNotice(@PathVariable Integer noticeId) {
        try {
            log.info(">>> 공지사항 숨기기 요청: {}", noticeId);
            noticeService.hideNotice(noticeId);

            AdminNoticeDto updatedNotice = noticeService.getNoticeById(noticeId);
            return ResponseEntity.ok(updatedNotice);
        } catch (Exception e) {
            log.error(">>> 공지사항 숨기기 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 공지사항 보이기
     */
    @PatchMapping("/{noticeId}/show")
    public ResponseEntity<AdminNoticeDto> showNotice(@PathVariable Integer noticeId) {
        try {
            log.info(">>> 공지사항 보이기 요청: {}", noticeId);
            noticeService.showNotice(noticeId);

            AdminNoticeDto updatedNotice = noticeService.getNoticeById(noticeId);
            return ResponseEntity.ok(updatedNotice);
        } catch (Exception e) {
            log.error(">>> 공지사항 보이기 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    /**
     * 공지사항 목록 조회 (검색 + 페이징)
     */
    @GetMapping
    public ResponseEntity<Page<AdminNoticeDto>> getNoticeList(
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        log.info("공지사항 목록 조회 요청: keyword={}, page={}", keyword, pageable.getPageNumber());

        try {
            Page<AdminNoticeDto> notices = noticeService.getNoticeList(keyword, pageable);
            return ResponseEntity.ok(notices);
        } catch (Exception e) {
            log.error("공지사항 목록 조회 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 공지사항 핀 상태 변경
     */
    @PatchMapping("/{noticeId}/pin")
    public ResponseEntity<AdminNoticeDto> changePinStatus(
            @PathVariable Integer noticeId,
            @RequestParam boolean pinYn) {

        log.info("공지사항 핀 상태 변경 요청: noticeId={}, pinYn={}", noticeId, pinYn);

        try {
            AdminNoticeDto updatedNotice = noticeService.changePinStatus(noticeId, pinYn);
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
     */
    @GetMapping("/{noticeId}")
    public ResponseEntity<AdminNoticeDto> getNotice(@PathVariable Integer noticeId) {
        log.info("공지사항 단건 조회 요청: noticeId={}", noticeId);

        try {
            AdminNoticeDto notice = noticeService.getNoticeById(noticeId);
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