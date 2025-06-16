package com.dev.moyering.socialing.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class HostPostComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer commentId;

    @Column(nullable = false)
    private String content;
    private LocalDateTime createTime;
    @Column(nullable = false)
    @ColumnDefault("0")
    private byte isDeleted;

    @Column(nullable = false)
    private int parentId;
    private int userId;

    @ManyToOne
    @JoinColumn(name = "post_id",nullable = false)
    private HostPost post;

}
