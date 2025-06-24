package com.dev.moyering.socialing.service;

import com.dev.moyering.socialing.dto.CommentDto;
import com.dev.moyering.socialing.dto.FeedDto;
import com.dev.moyering.socialing.entity.Comment;
import com.dev.moyering.socialing.entity.Feed;
import com.dev.moyering.socialing.repository.CommentRepository;
import com.dev.moyering.socialing.repository.FeedRepository;
import com.dev.moyering.socialing.repository.LikeListRepository;
import com.dev.moyering.user.entity.User;
import com.dev.moyering.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.io.File;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService {

    private final FeedRepository feedRepository;
    private final CommentRepository commentRepository;
    private final LikeListRepository likeListRepository;
    private final UserRepository userRepository;

    private final EntityManager entityManager;

    @Value("${iupload.path}")
    private String iuploadPath;

    // 피드 전체
    @Override
    @Transactional
    public List<FeedDto> getFeeds(String sortType, String userId) throws Exception {
        return feedRepository.findFeeds(sortType, userId);
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

    @Override
    public List<FeedDto> getFeedsByUserId(Integer userId) throws Exception {
        List<Feed> feeds = feedRepository.findAllByUserUserIdOrderByCreateDateDesc(userId);
        return feeds.stream().map(Feed::toDto).collect(Collectors.toList());
    }

    @Override
    public List<FeedDto> getFeedsByNickname(String nickname) throws Exception {
        User user = userRepository.findByNickName(nickname).orElseThrow(() -> new Exception("유저를 찾을 수 없습니다 : " + nickname));
        return getFeedsByUserId(user.getUserId());
    }

    @Override
    @Transactional
    public Integer createFeed(FeedDto feedDto, List<MultipartFile> images) throws Exception {
        User writer = userRepository.findByUsername(feedDto.getWriterId())
                .orElseThrow(() -> new Exception("작성자 '" + feedDto.getWriterId() + "'를 찾을 수 없습니다."));

        Feed feed = feedDto.toEntity();
        feed.setUser(writer);
        if (images != null) {                                                          // 나중에 이미지 중복체크 넣기
            for (int i = 0; i < images.size() && i < 5; i++) {
                MultipartFile img = images.get(i);
                if (!img.isEmpty()) {
                    String filename = img.getOriginalFilename();
                    File file = new File(iuploadPath, filename);
                    img.transferTo(file);
                    String publicUrl = iuploadPath + filename;
                    switch (i) {
                        case 0:
                            feed.setImg1(publicUrl);
                            break;
                        case 1:
                            feed.setImg2(publicUrl);
                            break;
                        case 2:
                            feed.setImg3(publicUrl);
                            break;
                        case 3:
                            feed.setImg4(publicUrl);
                            break;
                        case 4:
                            feed.setImg5(publicUrl);
                            break;
                    }
                }
            }
        }

        feedRepository.save(feed);
        Integer feedNum = feed.getFeedId();
        entityManager.clear();
        return feedNum;
    }
}