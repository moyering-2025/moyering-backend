package com.dev.moyering.socialing.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class HostFeedDto {

    private Integer feedId;
    private String content;

    private String img1;
    private String img2;
    private String img3;
    private String img4;
    private String img5;

    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private Boolean isDeleted;

    private String tag1;
    private String tag2;
    private String tag3;
    private String tag4;
    private String tag5;

    private String category;

    private Integer hostId;
    private String hostName;
    private String hostProfile;
}
