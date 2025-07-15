package com.dev.moyering.admin.controller;

import com.dev.moyering.admin.dto.AdminClassDetailDto;
import com.dev.moyering.admin.dto.AdminClassDto;
import com.dev.moyering.admin.dto.AdminClassSearchCond;
import com.dev.moyering.host.service.HostClassService;
import com.nimbusds.jose.util.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Path;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/class")
@Slf4j
@CrossOrigin(origins = "*")
public class AdminClassController {
    private final HostClassService hostClassService;

    /*** 클래스 리스트 조회*/
    @GetMapping
    public ResponseEntity<Page<AdminClassDto>> getClassList(
            @ModelAttribute AdminClassSearchCond cond,// 쿼리 파라미터나 폼데이터에서 데이터 가져오기
            @PageableDefault(size = 20, sort = "regDate", direction = Sort.Direction.DESC) Pageable pageable // 등록일 내림차순
    ) {
        try {
            Page<AdminClassDto> result = hostClassService.getHostClassListForAdmin(cond, pageable);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("클래스 목록 조회 중 오류 발생", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /*** 관리자가 강사 클래스를 승인 */
    @PatchMapping("/{classId}/approve")
    public ResponseEntity<?> approveClass(@PathVariable Integer classId) {
        try {
            hostClassService.approveClass(classId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("<UNK> <UNK> <UNK> <UNK> <UNK> <UNK>", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /*** 관리자가 강사 클래스를 거절 */
    @PatchMapping("/{classId}/reject")
    public ResponseEntity<String> rejectClass(@PathVariable Integer classId) {
        try {
            hostClassService.rejectClass(classId);
            return ResponseEntity.ok("반려");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    /*** 클래스 상세페이지 조회  */
    @GetMapping("/{classId}/detail")
    public ResponseEntity<AdminClassDetailDto> getClassDetail(@PathVariable Integer classId) {
        try {
            AdminClassDetailDto classDetail = hostClassService.getClassDetailForAdmin(classId);
            log.info("포드폴리오 파일명: {}", classDetail.getPortfolioName());
            log.info("강의자료 파일명: {}", classDetail.getMaterialName());
            return ResponseEntity.ok(classDetail);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}


