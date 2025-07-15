package com.dev.moyering.socialing.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder
public class ScrapListDto {

    private Integer scrapId;
    private Integer feedId;
    private String content;
    private String img1;
    private LocalDateTime createDate;
    private Integer writerUserId;
    private String writerNickName;
    private String writerProfile;
    private String writerBadgeImg;

    @QueryProjection
    public ScrapListDto(Integer scrapId, Integer feedId, String content, String img1, LocalDateTime createDate,
                        Integer writerUserId, String writerNickName, String writerProfile,String writerBadgeImg) {
        this.scrapId = scrapId;
        this.feedId = feedId;
        this.content = content;
        this.img1 = img1;
        this.createDate = createDate;
        this.writerUserId = writerUserId;
        this.writerNickName = writerNickName;
        this.writerProfile = writerProfile;
        this.writerBadgeImg = writerBadgeImg;
    }

}
