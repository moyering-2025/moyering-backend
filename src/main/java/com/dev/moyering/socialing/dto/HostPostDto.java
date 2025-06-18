package com.dev.moyering.socialing.dto;

import com.dev.moyering.host.entity.Host;

import javax.persistence.*;
import java.time.LocalDateTime;

public class HostPostDto {

    private int feedId;
    private String content;
    private String img1;
    private String img2;
    private String img3;
    private String img4;
    private String img5;
    private LocalDateTime createDate;
    private boolean isDeleted = false;
    private String tag1;
    private String tag2;
    private String tag3;
    private String tag4;
    private String tag5;

//    private
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "hostId", nullable = false)
//    private Host host;
}
