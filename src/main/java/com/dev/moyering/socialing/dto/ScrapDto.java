package com.dev.moyering.socialing.dto;

import com.dev.moyering.socialing.entity.Feed;
import com.dev.moyering.socialing.entity.Scrap;
import com.dev.moyering.user.entity.User;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ScrapDto {
    private Integer scrapId;
    private Integer userId;
    private Integer feedId;

    public Scrap toEntity() {
        Scrap entity = Scrap.builder()
                .scrapId(scrapId)
                .user(User.builder().userId(userId).build())
                .feed(Feed.builder().feedId(feedId).build())
                .build();
        return entity;
    }
}
