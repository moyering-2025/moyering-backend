package com.dev.moyering.socialing.repository;

import com.dev.moyering.socialing.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    // 특정 피드에 달린 댓글을 작성 시간 순으로 조회
    List<Comment> findByFeed_FeedIdOrderByCreateAtAsc(Integer feedId);

    long countByFeedFeedIdAndIsDeletedFalse(Integer feedId);

    List<Comment> findByFeedFeedIdAndParentIdIsNullAndIsDeletedFalseOrderByCreateAtAsc(Integer feedId);

    List<Comment> findByParentIdAndIsDeletedFalseOrderByCreateAtAsc(Integer parentId);
}
