package com.dev.moyering.entity.Socailing;

import com.dev.moyering.entity.common.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class LikeList {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Integer likeid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id",nullable = false)
    private Feed socialing;
}
