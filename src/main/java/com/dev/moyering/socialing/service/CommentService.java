package com.dev.moyering.socialing.service;

import com.dev.moyering.socialing.dto.CommentDto;
import com.dev.moyering.socialing.entity.Comment;

import java.util.List;

public interface CommentService {
    List<CommentDto> getComments(Integer feedId);
    void saveComment(CommentDto commentDto) throws Exception;
    void deleteComment(Integer commentId, Integer userId) throws Exception;
    CommentDto addComment(Integer feedId, Integer userId,String content,Integer parentId) throws Exception;
}
