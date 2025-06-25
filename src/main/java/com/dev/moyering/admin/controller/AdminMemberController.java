package com.dev.moyering.admin.controller;

import com.dev.moyering.admin.dto.AdminMemberDto;
import com.dev.moyering.admin.dto.AdminMemberSearchCond;
import com.dev.moyering.user.service.UserService;
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
@RequestMapping("/api/member")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class AdminMemberController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<Page<AdminMemberDto>> getMemberList(
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 20, sort = "regDate", direction = Sort.Direction.DESC) Pageable pageable) {

        log.info("회원 목록 조회 요청: keyword={}, page={}", keyword, pageable.getPageNumber());

        try {
            // 검색 조건 객체 생성 (keyword 활용)
            AdminMemberSearchCond searchCond = AdminMemberSearchCond.builder()
                    .keyword(keyword)
                    .build();

            Page<AdminMemberDto> members = userService.getMemberList(searchCond, pageable);
            return ResponseEntity.ok(members);
        } catch (Exception e) {
            log.error("회원 목록 조회 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 회원 상세조회 (모달)
     */
    @GetMapping("/{userId}")
    public ResponseEntity<AdminMemberDto> getMemberDetail(@PathVariable Integer userId) {
        log.info("회원 상세정보 요청: id = {}", userId);
        try {
            AdminMemberDto member = userService.getMemberDetail(userId);
            return ResponseEntity.ok(member);
        } catch (Exception e) {
            log.error("회원 상세 조회 실패: id = {}, error = {}", userId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /*** 회원 상태 변경 (활성화/비활성화)*/
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateMemberStatus(
            @PathVariable Integer userId,
            @RequestParam String status) {
        log.info("회원 상태 변경 요청: id = {}, status = {}", userId, status);
        try {
            userService.updateMemberStatus(userId, status);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("회원 상태 변경 실패: id = {}, status = {}, error = {}", userId, status, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}