package com.dev.moyering.socialing.dto;

import com.dev.moyering.common.entity.User;
import com.dev.moyering.socialing.entity.Feed;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FeedDto {

    private Integer feedId;
    private String content;
    private String img1;
    private String img2;
    private String img3;
    private String img4;
    private String img5;
    private String tag1;
    private String tag2;
    private String tag3;
    private String tag4;
    private String tag5;
    private boolean isDeleted;
    private String writerId;
    private String writerProfile;
    private Integer writerBadge;
    private Long commentsCount;
    private Long likesCount;

    // 피드 상세 페이지용
    private LocalDateTime createdAt;
    private Boolean likedByUser;       // 로그인 유저가 좋아요 눌렀는지
    private Boolean mine;              // 내 피드인지
    private List<CommentDto> comments;

    public Feed toEntity() {
        Feed entity = Feed.builder()
                .feedId(feedId)
                .content(content)
                .img1(img1)
                .img2(img2)
                .img3(img3)
                .img4(img4)
                .img5(img5)
                .tag1(tag1)
                .tag2(tag2)
                .tag3(tag3)
                .tag4(tag4)
                .tag5(tag5)
                .isDeleted(false)
                .user(User.builder().id(writerId).profile(writerProfile).userBadgeId(writerBadge).build())
                .build();
        return entity;
    }
}

