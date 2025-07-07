package com.dev.moyering.socialing.service;

import com.dev.moyering.host.entity.Host;
import com.dev.moyering.host.repository.HostRepository;
import com.dev.moyering.socialing.dto.HostFeedCreateDto;
import com.dev.moyering.socialing.dto.HostFeedDetailDto;
import com.dev.moyering.socialing.dto.HostFeedDto;
import com.dev.moyering.socialing.dto.HostFeedResponseDto;
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
    public List<HostFeedDto> getHostFeeds(int offset, int size,String category) throws Exception {
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
                        case 0: feed.setImg1(filename); break;
                        case 1: feed.setImg2(filename); break;
                        case 2: feed.setImg3(filename); break;
                        case 3: feed.setImg4(filename); break;
                        case 4: feed.setImg5(filename); break;
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
}
