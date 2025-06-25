package com.dev.moyering.socialing.dto;

import com.dev.moyering.socialing.entity.Feed;
import com.dev.moyering.socialing.entity.LikeList;
import com.dev.moyering.user.entity.User;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LikeListDto {

    private Integer likeid;
    private Integer userid;
    private Integer feedid;

    public LikeList toEntity() {
        LikeList entity = LikeList.builder()
                .likeid(likeid)
                .user(User.builder().userId(userid).build())
                .feed(Feed.builder().feedId(feedid).build())
                .build();
        return entity;
    }
}
