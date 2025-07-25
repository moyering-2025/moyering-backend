package com.dev.moyering.socialing.entity;

import java.time.LocalDateTime;

import javax.persistence.*;

import com.dev.moyering.socialing.dto.FeedDto;
import com.dev.moyering.user.entity.User;

import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Feed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer feedId;

    @Column(nullable = false,length = 500)
    private String content;

    @Column(nullable = false)
    private String img1;

    @Column
    private String img2;
    private String img3;
    private String img4;
    private String img5;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createDate;
    @Column
    private LocalDateTime updateDate;

    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean isDeleted;

    @Column
    private String tag1;
    private String tag2;
    private String tag3;
    private String tag4;
    private String tag5;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @PrePersist
    public void onCreate() {
        this.createDate = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updateDate = LocalDateTime.now();
    }

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
               .isDeleted(isDeleted)
               .writerId(user.getNickName())
               .writerProfile(user.getProfile())
               .writerBadge(user.getUserBadgeId())
               .writerUserId(user.getUserId())
               .build();
    }
}
