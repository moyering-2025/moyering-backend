package com.dev.moyering.socialing.dto;

import com.dev.moyering.socialing.entity.Feed;
import com.dev.moyering.user.entity.User;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    private Integer writerUserId;
    private String writerBadgeImg;

    // 피드 상세 페이지용
    private LocalDateTime createdAt;
    private Boolean likedByUser;       // 로그인 유저가 좋아요 눌렀는지
    private Boolean mine;              // 내 피드인지
    private List<CommentDto> comments;

    // 작성자(other feed) img1 리스트 (썸네일)
    private List<String> moreImg1List;

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
                .user(User.builder().nickName(writerId).profile(writerProfile).userBadgeId(writerBadge).userId(writerUserId).build())
                .build();
        return entity;
    }

    public FeedDto(Integer feedId, String content, String img1, String img2, String img3, String img4, String img5,
                   String tag1, String tag2, String tag3, String tag4, String tag5, boolean isDeleted,
                   String writerId, String writerProfile, Integer writerBadge, LocalDateTime createdAt, Boolean likedByUser,String writerBadgeImg) {
        this.feedId = feedId;
        this.content = content;
        this.img1 = img1;
        this.img2 = img2;
        this.img3 = img3;
        this.img4 = img4;
        this.img5 = img5;
        this.tag1 = tag1;
        this.tag2 = tag2;
        this.tag3 = tag3;
        this.tag4 = tag4;
        this.tag5 = tag5;
        this.isDeleted = isDeleted;
        this.writerId = writerId;
        this.writerProfile = writerProfile;
        this.writerBadge = writerBadge;
        this.createdAt = createdAt;
        this.likedByUser = likedByUser;
        this.writerBadgeImg = writerBadgeImg;
    }
}

