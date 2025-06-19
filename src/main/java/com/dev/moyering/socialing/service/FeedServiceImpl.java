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

import java.util.List;
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


}
