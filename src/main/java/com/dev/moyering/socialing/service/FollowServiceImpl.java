package com.dev.moyering.socialing.service;

import com.dev.moyering.socialing.dto.FollowDto;
import com.dev.moyering.socialing.entity.Follow;
import com.dev.moyering.socialing.repository.FollowRepository;
import com.dev.moyering.user.entity.User;
import com.dev.moyering.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public FollowDto follow(Integer followerId, Integer followingId) throws Exception {
        if (followerId.equals(followingId)) {
            throw new Exception("자기 자신을 팔로우할 수 없습니다.");
        }
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new Exception("Follower 사용자(ID=" + followerId + ")를 찾을 수 없습니다."));
        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new Exception("Following 사용자(ID=" + followingId + ")를 찾을 수 없습니다."));

        followRepository.findByFollowerUserIdAndFollowingUserId(followerId, followingId)
                .ifPresent(f -> { throw new RuntimeException("이미 팔로우 중입니다."); });

        Follow follow = Follow.builder()
                .follower(follower)
                .following(following)
                .build();
        Follow saved = followRepository.save(follow);
        return saved.toDto();
    }

    @Override
    public void unfollow(Integer followerId, Integer followingId) throws Exception {
        Follow follow = followRepository.findByFollowerUserIdAndFollowingUserId(followerId, followingId)
                .orElseThrow(() -> new Exception("팔로우 관계를 찾을 수 없습니다."));
        followRepository.delete(follow);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isFollowing(Integer followerId, Integer followingId) {
        return followRepository.findByFollowerUserIdAndFollowingUserId(followerId, followingId).isPresent();
    }

    @Override
    @Transactional
    public List<FollowDto> getFollowings(Integer followerId) {
        return followRepository.findAllByFollowerUserId(followerId).stream()
                .map(Follow::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<FollowDto> getFollowers(Integer followingId) {
        return followRepository.findAllByFollowingUserId(followingId).stream()
                .map(Follow::toDto)
                .collect(Collectors.toList());
    }
}
