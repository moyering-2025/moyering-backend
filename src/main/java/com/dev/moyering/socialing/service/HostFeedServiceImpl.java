package com.dev.moyering.socialing.service;

import com.dev.moyering.host.entity.Host;
import com.dev.moyering.host.repository.HostRepository;
import com.dev.moyering.socialing.dto.*;
import com.dev.moyering.socialing.entity.HostFeed;
import com.dev.moyering.socialing.repository.HostFeedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HostFeedServiceImpl implements HostFeedService {

    private final HostFeedRepository hostFeedRepository;
    private final HostRepository hostRepository;

    private final EntityManager entityManager;
    @Value("${iupload.path}")
    private String iuploadPath;

    @Override
    public List<HostFeedDto> getHostFeeds(int offset, int size, String category) throws Exception {
        if (category != null && !category.isEmpty()) {
            return hostFeedRepository.findHostFeedsByCategory(category, offset, size);
        } else {
            return hostFeedRepository.findHostFeeds(offset, size);
        }
    }

    @Override
    public HostFeedDetailDto getHostFeedDetail(Integer feedId) throws Exception {
        HostFeedDetailDto detail = hostFeedRepository.findHostFeedDetailById(feedId);

        if (detail == null) {
            throw new Exception("해당 피드를 찾을 수 없습니다.");
        }

        return detail;
    }

    @Override
    @Transactional
    public HostFeedResponseDto createHostFeed(Integer userId, HostFeedCreateDto dto, List<MultipartFile> images) throws Exception {
// 1) 업로드 디렉토리 생성
        File uploadDir = new File(iuploadPath);
        if (!uploadDir.exists()) {
            boolean created = uploadDir.mkdirs();
            if (!created) {
                throw new IOException("업로드 디렉터리 생성 실패: " + uploadDir.getAbsolutePath());
            }
        }

        // 2) userId 로 host 찾기
        Host host = hostRepository.findByUserId(userId)
                .orElseThrow(() -> new Exception("강사 정보가 존재하지 않습니다."));

        // 3) HostFeed 엔티티 생성
        HostFeed feed = HostFeed.builder()
                .content(dto.getContent())
                .tag1(dto.getTag1())
                .tag2(dto.getTag2())
                .tag3(dto.getTag3())
                .tag4(dto.getTag4())
                .tag5(dto.getTag5())
                .category(dto.getCategory())
                .isDeleted(false)
                .host(host)
                .build();

        // 4) 이미지 저장
        if (images != null) {
            for (int i = 0; i < images.size() && i < 5; i++) {
                MultipartFile img = images.get(i);
                if (!img.isEmpty()) {
                    String filename = img.getOriginalFilename();
                    File file = new File(iuploadPath, filename);
                    img.transferTo(file);

                    switch (i) {
                        case 0:
                            feed.setImg1(filename);
                            break;
                        case 1:
                            feed.setImg2(filename);
                            break;
                        case 2:
                            feed.setImg3(filename);
                            break;
                        case 3:
                            feed.setImg4(filename);
                            break;
                        case 4:
                            feed.setImg5(filename);
                            break;
                    }
                }
            }
        }

        // 5) DB 저장
        hostFeedRepository.save(feed);

        // 6) 영속성 컨텍스트 초기화 (선택)
//        entityManager.clear();

        // 7) 저장된 엔티티를 DTO 로 변환해 반환
        return HostFeedResponseDto.builder()
                .feedId(feed.getFeedId())
                .content(feed.getContent())
                .img1(feed.getImg1())
                .img2(feed.getImg2())
                .img3(feed.getImg3())
                .img4(feed.getImg4())
                .img5(feed.getImg5())
                .tag1(feed.getTag1())
                .tag2(feed.getTag2())
                .tag3(feed.getTag3())
                .tag4(feed.getTag4())
                .tag5(feed.getTag5())
                .category(feed.getCategory())
                .build();
    }

    @Override
    @Transactional
    public void updateHostFeed(Integer feedId, Integer userId, HostFeedUpdateDto dto, List<MultipartFile> images,List<String> removeUrls) throws Exception {
        HostFeed feed = hostFeedRepository.findById(feedId)
                .orElseThrow(() -> new Exception("Feed not found"));

        // 권한 체크: 강사의 userId
        if (!feed.getHost().getUserId().equals(userId)) {
            throw new Exception("권한이 없습니다.");
        }

        // 내용 업데이트
        feed.setContent(dto.getContent());
        feed.setTag1(dto.getTag1());
        feed.setTag2(dto.getTag2());
        feed.setTag3(dto.getTag3());
        feed.setTag4(dto.getTag4());
        feed.setTag5(dto.getTag5());
        feed.setCategory(dto.getCategory());

        // 이미지 저장 (기존 방식 재활용)
        List<String> existingImages = new ArrayList<>();
        if (feed.getImg1() != null) existingImages.add(feed.getImg1());
        if (feed.getImg2() != null) existingImages.add(feed.getImg2());
        if (feed.getImg3() != null) existingImages.add(feed.getImg3());
        if (feed.getImg4() != null) existingImages.add(feed.getImg4());
        if (feed.getImg5() != null) existingImages.add(feed.getImg5());

        // removeUrls 로 지운 것들 제외
        if (removeUrls != null) {
            existingImages.removeIf(removeUrls::contains);
        }

        // 새 업로드 파일 추가
        if (images != null) {
            for (MultipartFile img : images) {
                if (!img.isEmpty()) {
                    String filename = img.getOriginalFilename();
                    img.transferTo(new File(iuploadPath, filename));
                    existingImages.add(filename);
                }
            }
        }

        // 이미지 5개 제한
        while (existingImages.size() > 5) existingImages.remove(5);

        // 모든 img를 null 초기화
        feed.setImg1(null); feed.setImg2(null);
        feed.setImg3(null); feed.setImg4(null); feed.setImg5(null);

        // 다시 순서대로 채우기
        for (int i = 0; i < existingImages.size(); i++) {
            switch (i) {
                case 0: feed.setImg1(existingImages.get(0)); break;
                case 1: feed.setImg2(existingImages.get(1)); break;
                case 2: feed.setImg3(existingImages.get(2)); break;
                case 3: feed.setImg4(existingImages.get(3)); break;
                case 4: feed.setImg5(existingImages.get(4)); break;
            }
        }}

    @Override
    @Transactional
    public void deleteHostFeed(Integer feedId, Integer userId) throws Exception{
        HostFeed feed = hostFeedRepository.findById(feedId)
                .orElseThrow(() -> new Exception("Feed not found"));

        Host host = hostRepository.findByUserId(userId)
                .orElseThrow(() -> new Exception("강사 정보가 존재하지 않습니다."));

        if (!feed.getHost().getHostId().equals(host.getHostId())) {
            throw new Exception("권한이 없습니다.");
        }

        feed.setIsDeleted(true);
    }
}
