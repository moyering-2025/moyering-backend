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
    private Integer commentId;

    @Column(nullable = false)
    private String content;
    private LocalDateTime createTime;
    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean isDeleted = false;

    @Column(nullable = false)
    private int parentId;
    private int userId;

    @ManyToOne
    @JoinColumn(name = "postId", nullable = false)
    private HostPost post;

}
