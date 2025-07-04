package com.dev.moyering.admin.controller;

import com.dev.moyering.admin.dto.BannerDto;
import com.dev.moyering.admin.entity.Banner;
import com.dev.moyering.admin.service.BannerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/banner")
@Slf4j
@CrossOrigin(origins = "*")
public class AdminBannerController {
    private final BannerService bannerService;

    @Value("${iupload.path}")
    private String bannerUploadPath;

    /*** 배너 등록*/
    @PostMapping("/create")
    public ResponseEntity<BannerDto> bannerCreate(BannerDto bannerDto,
                                                  @RequestPart(name = "ifile", required = false) MultipartFile bannerImg) {

        log.info("배너 등록 요청 : {}", bannerDto.getBannerId());
        log.info(bannerImg != null ? bannerImg.getOriginalFilename() : "null",
                bannerImg != null ? bannerImg.getSize() : 0,
                bannerImg != null ? bannerImg.isEmpty() : "null");

        try {
            // 배너 생성 후 바로 생성된 배너 정보 반환
            BannerDto createBanner = bannerService.createBanner(bannerDto, bannerImg);
            return ResponseEntity.ok(createBanner);
        } catch (Exception e) {
            log.error("배너 등록 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

        /*** 배너 수정*/
        @PutMapping("/{bannerId}")
        public ResponseEntity<BannerDto> bannerEdit(
//            @ReqestBody // form-data와 @RequestBody 동시사용 불가 !
                // 개별파라미터로 받기
                @PathVariable Integer bannerId,
                @RequestParam(required = false) String title,
                @RequestParam(required = false) String content,
//                @RequestPart(name="bannerDto") BannerDto bannerDto,
                @RequestPart(name = "ifile", required = false) MultipartFile bannerImg) {
            log.info("배너 수정 요청 - bannerId : {}, title : {}, content : {}, bannerImg : {}", bannerId, title, content,  bannerImg);
            log.info("받은 파일: {}", bannerImg != null ? bannerImg.getOriginalFilename() : "파일 없음");

            try {
                // DTO 생성
                BannerDto bannerDto = new BannerDto();
                bannerDto.setBannerId(bannerId);
                bannerDto.setTitle(title);
                bannerDto.setContent(content);

                // 서비스 호출
                BannerDto editBanner = bannerService.updateBanner(bannerDto, bannerImg);
                return ResponseEntity.ok(editBanner);
            } catch (IllegalArgumentException e) {
                log.error("배너 수정 실패 : {}", e.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            } catch (Exception e) {
                log.error("배너 수정 중 오류 발생 : {}", e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }


        /*** 배너 삭제*/
        @DeleteMapping("/{bannerId}")
        public ResponseEntity<Void> deleteBanner(@PathVariable Integer bannerId) {
            log.info("배너 삭제 요청: {}", bannerId);
            try {
                bannerService.deleteBanner(bannerId);
                return ResponseEntity.noContent().build(); // 204
            } catch (IllegalArgumentException e) {
                log.error("배너 삭제 실패 - 존재하지 않음: {}", e.getMessage());
                return ResponseEntity.notFound().build(); // 404
            } catch (Exception e) {
                log.error("배너 삭제 실패: {}", e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500
            }
        }

        /*** 배너 리스트 조회 */
        @GetMapping
        public ResponseEntity<Page<BannerDto>> getBannerList(
                @RequestParam(required = false) String keyword,
                @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
            log.info("배너 목록 조회 요청 : keyword = {}, page = {}", keyword, pageable.getPageNumber());

            try {
                // 검색 조건 객체 생성 (keyword 활용)
                Page<BannerDto> banner = bannerService.findBannerByKeyword(keyword, pageable);
                return ResponseEntity.ok(banner);
            } catch (Exception e) {
                log.error("배너 조회 실패 : {}", e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

        /*** 단건 조회 */
        @GetMapping("/{bannerId}") // 단건 조회
        public ResponseEntity<BannerDto> getBannerById(@PathVariable Integer bannerId) {
            log.info("공지사항 단건 조회 요청 : bannerId = {}", bannerId);

            try {
                BannerDto banner = bannerService.findBannerByBannerId(bannerId);
                return ResponseEntity.ok(banner);

            } catch (IllegalArgumentException e) {
                log.error("배너 단건 조회 요청 실패 : bannerId = {}", bannerId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            } catch (Exception e) {
                log.error("배너 조회 실패 : {}", e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

        /*** 배너 숨기기 */
        @PatchMapping("/{bannerId}/hide")
        public ResponseEntity<BannerDto> hideBanner(@PathVariable Integer bannerId) {
            try {
                log.info(">>> 배너 숨기기 요청 :{}", bannerId);
                bannerService.hideBanner(bannerId);

                BannerDto updateBanner = bannerService.findBannerByBannerId(bannerId);
                return ResponseEntity.ok(updateBanner);
            } catch (Exception e) {
                log.error("배너 숨기기 실패 : {}", e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

        /*** 배너 보이기 */
        @PatchMapping("/{bannerId}/show")
        public ResponseEntity<BannerDto> showBanner(@PathVariable Integer bannerId) {
            try {
                log.info(">>> 배너 보이기 요청 :{}", bannerId);
                bannerService.showBanner(bannerId);

                BannerDto updateBanner = bannerService.findBannerByBannerId(bannerId);
                return ResponseEntity.ok(updateBanner);
            } catch (Exception e) {
                log.error("배너 보이기 실패 : {}", e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

    /*** 배너 이미지 서빙*/
    @GetMapping("/uploads/{filename}")
    public ResponseEntity<byte[]> getBannerImage(@PathVariable String filename) {
        log.info("이미지 요청: {}", filename);

        try {
            // 파일 경로 생성
            Path filePath = Paths.get(bannerUploadPath).resolve(filename);
            File file = filePath.toFile();

            log.info("파일 전체 경로: {}", filePath.toString());
            log.info("파일 존재 여부: {}", file.exists());

            if (!file.exists()) {
                log.warn("파일이 존재하지 않음: {}", filePath.toString());
                return ResponseEntity.notFound().build();
            }

            // 파일을 바이트 배열로 읽기
            byte[] imageBytes = Files.readAllBytes(filePath);

            // Content-Type 설정
            HttpHeaders headers = new HttpHeaders();
            String lowerFilename = filename.toLowerCase();

            if (lowerFilename.endsWith(".png")) {
                headers.setContentType(MediaType.IMAGE_PNG);
            } else if (lowerFilename.endsWith(".gif")) {
                headers.setContentType(MediaType.IMAGE_GIF);
            } else if (lowerFilename.endsWith(".jpg") || lowerFilename.endsWith(".jpeg")) {
                headers.setContentType(MediaType.IMAGE_JPEG);
            } else {
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            }

            log.info("이미지 반환 성공: {}, 크기: {} bytes", filename, imageBytes.length);
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(imageBytes);

        } catch (IOException e) {
            log.error("이미지 파일 읽기 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            log.error("이미지 서빙 중 오류: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
