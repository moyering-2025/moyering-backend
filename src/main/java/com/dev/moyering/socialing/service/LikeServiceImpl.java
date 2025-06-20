package com.dev.moyering.socialing.service;


import com.dev.moyering.socialing.entity.Feed;
import com.dev.moyering.socialing.entity.LikeList;
import com.dev.moyering.socialing.repository.FeedRepository;
import com.dev.moyering.socialing.repository.LikeListRepository;
import com.dev.moyering.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeListRepository likeListRepository;
    private final FeedRepository feedRepository;

    @Transactional
    @Override
    public void toggleLike(Integer feedId, Integer userId) throws Exception {
        boolean exists = likeListRepository.existsByFeedFeedIdAndUserUserId(feedId, userId);

        if (exists) {
            likeListRepository.deleteByFeed_FeedIdAndUser_Username(feedId, userId);
        }else {
            Feed feed = feedRepository.findById(feedId).orElseThrow(
                    () -> new Exception("피드가 존재하지 않습니다.")
            );

            LikeList like = LikeList.builder()
                    .feed(feed)
                    .user(User.builder().userId(userId).build())
                    .build();

            likeListRepository.save(like);
        }
    }


}
