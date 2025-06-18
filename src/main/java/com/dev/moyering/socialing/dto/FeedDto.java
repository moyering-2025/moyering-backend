package com.dev.moyering.socialing.dto;

import com.dev.moyering.socialing.entity.Feed;
import com.dev.moyering.user.entity.User;

import lombok.*;

import java.time.LocalDateTime;

@Getter
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
    private LocalDateTime createDate;
    private String writerId; // 작성자 이름
    private String writerProfile;  // 작성자 프로필
    private Integer writerBadge;
    private Integer commentsCount;
    private Integer likesCount;

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
                .createDate(createDate != null ? createDate : LocalDateTime.now())
                .isDeleted(false)
                .user(User.builder().username(writerId).profile(writerProfile).userBadgeId(writerBadge).build())
                .build();
        return entity;
    }
}

