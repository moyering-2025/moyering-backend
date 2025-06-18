package com.dev.moyering.socialing.dto;

import com.dev.moyering.common.entity.User;
import com.dev.moyering.socialing.entity.Comment;
import com.dev.moyering.socialing.entity.Feed;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Getter
@Builder
public class CommentDto {

    private Integer commentId;
    private LocalDateTime createAt;
    private String content;
    private Integer parentId;
    private boolean isDeleted;

    private Integer feedId;
    private Integer userId;
    private String id;
    private Integer userBadge;

    public Comment toEntity() {
        Comment entity = Comment.builder()
                .commentId(commentId)
                .content(content)
                .parentId(parentId)
                .isDeleted(false)
                .createAt(createAt != null ? createAt : LocalDateTime.now())
                .user(User.builder().userId(userId).id(id).userBadgeId(userBadge).build())
                .feed(Feed.builder().feedId(feedId).build())
                .build();
        return entity;
    }
}
