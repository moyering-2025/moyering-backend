package com.dev.moyering.socialing.service;

import com.dev.moyering.socialing.dto.ScrapDto;
import com.dev.moyering.socialing.entity.Scrap;
import com.dev.moyering.socialing.repository.ScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Table;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScrapServiceImpl implements ScrapService {

    private final ScrapRepository scrapRepository;

    @Override
    @Transactional
    public ScrapDto createScrap(Integer userId, Integer feedId) throws Exception {
        Optional<Scrap> existing = scrapRepository.findScrapByUserIdAndFeedId(userId, feedId);
        if (existing.isPresent()) {
            throw new Exception("이미 스크랩된 피드입니다.");
        }
        ScrapDto dto = ScrapDto.builder()
                .userId(userId)
                .feedId(feedId)
                .build();
        Scrap saved = scrapRepository.save(dto.toEntity());
        return saved.toDto();
    }

    @Override
    @Transactional
    public void deleteScrap(Integer userId, Integer feedId) throws Exception {
        Optional<Scrap> existing = scrapRepository.findScrapByUserIdAndFeedId(userId, feedId);
        if (existing.isEmpty()) {
            throw new Exception("스크랩이 존재하지 않습니다.");
        }
        // QueryDSL delete 호출
        scrapRepository.deleteByUserIdAndFeedId(userId, feedId);
    }

    @Override
    public List<Integer> getScrapFeedIds(Integer userId) {
        return scrapRepository.findFeedIdsByUserId(userId);
    }

    @Override
    public boolean isScrapped(Integer userId, Integer feedId) {
        return scrapRepository.findScrapByUserIdAndFeedId(userId, feedId).isPresent();
    }
}
