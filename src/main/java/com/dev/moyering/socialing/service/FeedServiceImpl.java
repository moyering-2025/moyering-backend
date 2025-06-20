package com.dev.moyering.socialing.service;

import com.dev.moyering.socialing.dto.CommentDto;
import com.dev.moyering.socialing.dto.FeedDto;
import com.dev.moyering.socialing.entity.Comment;
import com.dev.moyering.socialing.entity.Feed;
import com.dev.moyering.socialing.repository.CommentRepository;
import com.dev.moyering.socialing.repository.FeedRepository;
import com.dev.moyering.socialing.repository.LikeListRepository;
import com.dev.moyering.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService {

    private final FeedRepository feedRepository;
    private final CommentRepository commentRepository;
    private final LikeListRepository likeListRepository;

    // 피드 전체
    @Override
    public List<FeedDto> getFeeds(String sortType, String userId) throws Exception {
        return feedRepository.findFeeds(sortType, userId);
    }

    //피드 상세 조회
    @Override
    public FeedDto getFeedById(Integer feedId) throws Exception {
        Feed feed = feedRepository.findById(feedId).orElseThrow(() -> new Exception("해당 피드가 존재하지 않습니다."));

        User writer = feed.getUser();
        return null;
    }

    @Override
    public List<CommentDto> getCommentsByFeedId(Integer feedId) throws Exception {
        return List.of();
    }

    @Override
    public Long getLikeCountByFeedId(Integer feedId) throws Exception {
        return 0L;
    }

    @Override
    public boolean checkUserLikeFeed(Integer feedId, String userId) throws Exception {
        return false;
    }


    @Override
    @Transactional
    public FeedDto getFeedDetail(Integer feedId, Integer currentUserId) {
        // 1) Feed 조회
        Feed feed = feedRepository
                .findByFeedIdAndIsDeletedFalse(feedId)
                .orElseThrow(() -> new NoSuchElementException("Feed not found: " + feedId));

        // 2) FeedDto 기본 매핑
        FeedDto dto = feed.toDto();
        dto.setCreatedAt(feed.getCreateDate());
        dto.setMine(currentUserId != null &&
                feed.getUser().getUserId().equals(currentUserId));

        // 3) 좋아요/댓글 수, likedByUser
        dto.setLikesCount(likeListRepository.countByFeedFeedId(feedId));
        dto.setLikedByUser(currentUserId != null &&
                likeListRepository.existsByFeedFeedIdAndUserUserId(feedId, currentUserId)
        );
        dto.setCommentsCount(commentRepository.countByFeedFeedIdAndIsDeletedFalse(feedId));

        // 4) 댓글 + 대댓글 구성
        List<CommentDto> commentDtos = commentRepository
                .findByFeedFeedIdAndParentIdIsNullAndIsDeletedFalseOrderByCreateAtAsc(feedId)
                .stream()
                .map(parent -> {
                    CommentDto pd = parent.toDto();
                    List<CommentDto> replies = commentRepository
                            .findByParentIdAndIsDeletedFalseOrderByCreateAtAsc(parent.getCommentId())
                            .stream()
                            .map(Comment::toDto)
                            .collect(Collectors.toList());
                    pd.setReplies(replies);
                    return pd;
                })
                .collect(Collectors.toList());
        dto.setComments(commentDtos);

        // 5) 더 많은 게시물 썸네일(img1)
        Integer writerId = feed.getUser().getUserId();
        List<String> thumbs = feedRepository.findTop3Img1ByUserId(writerId);
        dto.setMoreImg1List(thumbs);

        return dto;
    }
}