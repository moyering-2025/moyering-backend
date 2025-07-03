package com.dev.moyering.socialing.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.dev.moyering.socialing.entity.Comment;
import com.dev.moyering.socialing.entity.Feed;
import com.dev.moyering.user.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    private Integer commentId;
    private LocalDateTime createAt;
    private String content;
    private Integer parentId;
    private boolean isDeleted;

    private Integer feedId;
    private Integer userId;
    private String writerId;
    private Integer userBadge;

    private List<CommentDto> replies;

    public Comment toEntity() {
        Comment entity = Comment.builder()
                .commentId(commentId)
                .content(content)
                .parentId(parentId)
                .isDeleted(false)
                .createAt(createAt != null ? createAt : LocalDateTime.now())
                .user(User.builder().userId(userId).nickName(writerId).userBadgeId(userBadge).build())
                .feed(Feed.builder().feedId(feedId).build())
                .build();
        return entity;
    }
}
