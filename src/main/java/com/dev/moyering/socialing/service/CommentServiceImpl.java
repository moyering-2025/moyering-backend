package com.dev.moyering.socialing.service;

import com.dev.moyering.socialing.dto.CommentDto;
import com.dev.moyering.socialing.entity.Comment;
import com.dev.moyering.socialing.entity.Feed;
import com.dev.moyering.socialing.repository.CommentRepository;
import com.dev.moyering.socialing.repository.FeedRepository;
import com.dev.moyering.user.entity.User;
import com.dev.moyering.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final FeedRepository feedRepository;
    private final UserRepository userRepository;


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
        return saved.toDto();
    }
}
