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

    /*** 공지사항 등록*/
    @PostMapping
    public ResponseEntity<AdminNoticeDto> createNotice(@RequestBody AdminNoticeDto noticeDto) {

        try {
            AdminNoticeDto createdNotice = noticeService.createNotice(noticeDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdNotice);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /*** 공지사항 수정*/
    @PutMapping("/{noticeId}")
    public ResponseEntity<AdminNoticeDto> updateNotice(
            @PathVariable Integer noticeId,
            @Valid @RequestBody AdminNoticeDto noticeDto) { // 유효성 검증
         // 공지사항 id 검증
        if (noticeDto.getNoticeId() != null && !noticeDto.getNoticeId().equals(noticeId)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

         // 검증 후 정상로직 수행
        try {
            AdminNoticeDto updatedNotice = noticeService.updateNotice(noticeId, noticeDto);
            return ResponseEntity.ok(updatedNotice);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /*** 공지사항 삭제*/
    @DeleteMapping("/{noticeId}")
    public ResponseEntity<Void> deleteNotice(@PathVariable Integer noticeId) {
        try {
            noticeService.deleteNotice(noticeId);
            return ResponseEntity.noContent().build(); // 204
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build(); // 404
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500
        }
    }



    /*** 공지사항 숨가가*/
    @PatchMapping("/{noticeId}/hide")
    public ResponseEntity<AdminNoticeDto> hideNotice(@PathVariable Integer noticeId) {
        try {
            noticeService.hideNotice(noticeId);

            AdminNoticeDto updatedNotice = noticeService.getNoticeById(noticeId);
            return ResponseEntity.ok(updatedNotice);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /*** 공지사항 보이기*/
    @PatchMapping("/{noticeId}/show")
    public ResponseEntity<AdminNoticeDto> showNotice(@PathVariable Integer noticeId) {
        try {
            noticeService.showNotice(noticeId);

            AdminNoticeDto updatedNotice = noticeService.getNoticeById(noticeId);
            return ResponseEntity.ok(updatedNotice);
        } catch (Exception e) {
            log.error(">>> 공지사항 보이기 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    /*** 공지사항 목록 조회 (검색 + 페이징)*/
    @GetMapping
    public ResponseEntity<Page<AdminNoticeDto>> getNoticeList(
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        try {
            Page<AdminNoticeDto> notices = noticeService.getNoticeList(keyword, pageable);
            return ResponseEntity.ok(notices);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /*** 공지사항 핀 상태 변경*/
    @PatchMapping("/{noticeId}/pin")
    public ResponseEntity<AdminNoticeDto> changePinStatus(
            @PathVariable Integer noticeId,
            @RequestParam boolean pinYn) {
        try {
            AdminNoticeDto updatedNotice = noticeService.changePinStatus(noticeId, pinYn);
            return ResponseEntity.ok(updatedNotice);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /*** 공지사항 단건 조회*/
    @GetMapping("/{noticeId}")
    public ResponseEntity<AdminNoticeDto> getNotice(@PathVariable Integer noticeId) {

        try {
            AdminNoticeDto notice = noticeService.getNoticeById(noticeId);
            return ResponseEntity.ok(notice);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}