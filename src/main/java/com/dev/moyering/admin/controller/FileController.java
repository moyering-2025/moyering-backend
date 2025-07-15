package com.dev.moyering.admin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.File;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class FileController {

    @Value("${iupload.path}")
    private String uploadPath;

    // 파일 다운로드 - PPT, 포트폴리오 등
    @GetMapping("/download/{filename}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        try {
            File file = new File(uploadPath, filename);

            if (!file.exists()) {
                log.error("파일을 찾을 수 없습니다: {}", filename);
                return ResponseEntity.notFound().build();
            }

            Resource resource = new FileSystemResource(file);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .body(resource);

        } catch (Exception e) {
            log.error("파일 다운로드 오류: {}", filename, e);
            return ResponseEntity.internalServerError().build();
        }
    }
}