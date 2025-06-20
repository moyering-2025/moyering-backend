package com.dev.moyering.socialing.entity;

import javax.persistence.*;

import com.dev.moyering.user.entity.User;

import com.dev.moyering.socialing.dto.CommentDto;
import lombok.*;

import java.time.LocalDateTime;

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

    @PrePersist
    public void prePersist() {
        if (createAt == null) {
            this.createAt = LocalDateTime.now();
        }
    }
    public CommentDto toDto() {
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
                .build();
    }
}
