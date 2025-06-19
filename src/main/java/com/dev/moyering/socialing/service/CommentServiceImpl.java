package com.dev.moyering.socialing.service;

import com.dev.moyering.socialing.dto.CommentDto;
import com.dev.moyering.socialing.entity.Comment;
import com.dev.moyering.socialing.repository.CommentRepository;
import com.dev.moyering.socialing.repository.FeedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final FeedRepository feedRepository;


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
}
