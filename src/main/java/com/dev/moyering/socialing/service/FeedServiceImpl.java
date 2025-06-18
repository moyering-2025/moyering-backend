package com.dev.moyering.socialing.service;

import com.dev.moyering.socialing.dto.CommentDto;
import com.dev.moyering.socialing.dto.FeedDto;
import com.dev.moyering.socialing.entity.Comment;
import com.dev.moyering.socialing.entity.Feed;
import com.dev.moyering.socialing.repository.CommentRepository;
import com.dev.moyering.socialing.repository.FeedRepository;
import com.dev.moyering.socialing.repository.LikeListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService {

    private final FeedRepository feedRepository;
    private final CommentRepository commentRepository;
    private final LikeListRepository likeRepository;

    // 피드 전체
    @Override
    public List<FeedDto> getFeeds(String sortType, String userId) throws Exception {
        return feedRepository.findFeeds(sortType, userId);
    }

//    // 피드 상세
//    @Override
//    public FeedDto getFeedDetail(Integer feedId, Integer userId) throws Exception {
//        Feed feed = feedRepository.findById(feedId)
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 피드입니다."));
//
//        // 기본 FeedDto 변환 (엔티티의 toDto 사용)
//        FeedDto dto = feed.toDto();
//
//        // 댓글 목록
//        List<CommentDto> comments = commentRepository.findByFeed_FeedIdOrderByCreateAtAsc(feedId)
//                .stream()
//                .map(Comment::toDto)  // CommentDto에 이 정적 메서드 필요
//                .collect(Collectors.toList());
//
//        // 로그인 사용자의 좋아요 여부
//        boolean likedByUser = false;
//        if (userId != null) {
//            likedByUser = likeRepository.existsByFeedFeedIdAndUserUserId(feedId, userId);
//        }
//
//        // DTO에 상세 정보 추가
//        dto.setCreatedAt(feed.getCreateDate());
//        dto.setComments(comments);
//        dto.setLikedByUser(likedByUser);
//        dto.setMine(userId != null && feed.getUser().getUserId().equals(userId));
//
//        return dto;
//    }

}
