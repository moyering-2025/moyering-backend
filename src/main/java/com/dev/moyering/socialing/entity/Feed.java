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

import com.dev.moyering.common.entity.User;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Feed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer feedId;

    @Column
    private String content;

    @Column(nullable = false)
    private String img1;

    @Column
    private String img2;
    private String img3;
    private String img4;
    private String img5;

    private LocalDateTime createDate;

    @Column(nullable = false)
    private byte deleted;

    private String tag1;
    private String tag2;
    private String tag3;
    private String tag4;
    private String tag5;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

}
