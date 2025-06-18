package com.dev.moyering.socialing.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.dev.moyering.socialing.dto.FeedDto;
import com.dev.moyering.user.entity.User;

import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Feed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer feedId;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String img1;

    @Column
    private String img2;
    private String img3;
    private String img4;
    private String img5;

    private LocalDateTime createDate;

    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean isDeleted = false;

    private String tag1;
    private String tag2;
    private String tag3;
    private String tag4;
    private String tag5;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public FeedDto toDto(){
       return FeedDto.builder()
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
               .createDate(createDate)
               .isDeleted(false)
               .writerId(user.getUsername())
               .writerProfile(user.getProfile())
               .writerBadge(user.getUserBadgeId())
               .build();
    }
}
