package com.dev.moyering.socialing.entity;

import javax.persistence.*;

import com.dev.moyering.user.entity.User;

import com.dev.moyering.socialing.dto.CommentDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feedId")
    private Feed feed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @Column(nullable = false)
    private LocalDateTime createAt;

    @Column(nullable = false)
    private String content;

    @Column
    private Integer parentId;

    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean isDeleted;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentId", referencedColumnName = "commentId", insertable = false, updatable = false)
    private List<Comment> replies;

    @PrePersist
    public void prePersist() {
        if (createAt == null) {
            this.createAt = LocalDateTime.now();
        }
    }
//    public CommentDto toDto() {
//        return CommentDto.builder()
//                .commentId(commentId)
//                .content(content)
//                .parentId(parentId)
//                .isDeleted(isDeleted)
//                .createAt(createAt)
//                .userId(user.getUserId())
//                .writerId(user.getNickName())
//                .userBadge(user.getUserBadgeId())
//                .feedId(feed.getFeedId())
//                .replies(replies != null
//                        ? replies.stream().map(Comment::toDto).collect(Collectors.toList())
//                        : new ArrayList<>())
//                .build();
//    }
public CommentDto toDto(List<Comment> allComments) {
    String parentWriter = null;
    if (this.parentId != null) {
        parentWriter = allComments.stream()
                .filter(c -> c.getCommentId().equals(this.parentId))
                .map(c -> c.getUser().getNickName())
                .findFirst()
                .orElse(null);
    }

    return CommentDto.builder()
            .commentId(commentId)
            .content(content)
            .parentId(parentId)
            .isDeleted(isDeleted)
            .createAt(createAt)
            .userId(user.getUserId())
            .writerId(user.getNickName())
            .userBadge(user.getUserBadgeId())
            .feedId(feed.getFeedId())
            .parentWriterId(parentWriter)
            .replies(replies != null
                    ? replies.stream().map(child -> child.toDto(allComments)).collect(Collectors.toList())
                    : new ArrayList<>())
            .build();
}
}
