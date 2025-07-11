package com.dev.moyering.socialing.service;

import com.dev.moyering.admin.service.AdminBadgeScoreService;
import com.dev.moyering.socialing.dto.CommentDto;
import com.dev.moyering.socialing.entity.Comment;
import com.dev.moyering.socialing.entity.Feed;
import com.dev.moyering.socialing.repository.CommentRepository;
import com.dev.moyering.socialing.repository.FeedRepository;
import com.dev.moyering.user.entity.User;
import com.dev.moyering.user.repository.UserRepository;
import com.dev.moyering.user.service.UserBadgeService;
import com.dev.moyering.user.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final FeedRepository feedRepository;
    private final UserRepository userRepository;

	private final UserService userService;
	private final AdminBadgeScoreService adminBadgeScoreService;
	private final UserBadgeService userBadgeService;
	
    @Override
    public List<CommentDto> getComments(Integer feedId) {
    return null;
    }

    @Transactional
    @Override
    public void saveComment(CommentDto commentDto) throws Exception {
        Comment comment = commentDto.toEntity();
        commentRepository.save(comment);
    }

    @Override
    public void deleteComment(Integer commentId, Integer userId) throws Exception {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new Exception("댓글이 존재하지 않습니다."));
        if (!comment.getUser().getUserId().equals(userId)) {
            throw new Exception("댓글을 삭제할 권한이 없습니다.");
        }
        comment.setDeleted(true);
    }

    @Transactional
    @Override
    public CommentDto addComment(Integer feedId, Integer userId, String content, Integer parentId) throws Exception {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new NoSuchElementException("해당 피드를 찾을 수 없습니다. feedId=" + feedId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("해당 사용자를 찾을 수 없습니다. userId=" + userId));
        if (content == null || content.isBlank()) {
            throw new Exception("댓글 내용을 입력해주세요.");
        }
        Comment comment = Comment.builder()
                .feed(feed)
                .user(user)
                .content(content)
                .parentId(parentId)
                .isDeleted(false)
                .build();

        Comment saved = commentRepository.save(comment);
        
        //소셜링 댓글 작성 시 포인트 획득
        //증가시킬 포인트 찾기
        Integer score = adminBadgeScoreService.getScoreByTitle("소셜링 댓글 작성");
        //유저의 활동점수 증가
        userService.addScore(comment.getUser().getUserId(), score);
        //뱃지 획득 가능 여부 확인
        userBadgeService.giveBadgeWithScore(comment.getUser().getUserId());

        // ---------- parentWriterId 채우기 ----------
        CommentDto dto = saved.toDto();
        if (parentId != null) {
            Comment parent = commentRepository.findById(parentId)
                    .orElseThrow(() -> new NoSuchElementException("부모 댓글을 찾을 수 없습니다. parentId=" + parentId));
            dto.setParentWriterId(parent.getUser().getNickName());
        }
        return saved.toDto();
    }
}
