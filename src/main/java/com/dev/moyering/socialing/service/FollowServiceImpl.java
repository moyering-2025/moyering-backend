package com.dev.moyering.socialing.service;

import com.dev.moyering.common.dto.AlarmDto;
import com.dev.moyering.common.service.AlarmService;
import com.dev.moyering.socialing.dto.FollowDto;
import com.dev.moyering.socialing.entity.Follow;
import com.dev.moyering.socialing.repository.FollowRepository;
import com.dev.moyering.socialing.repository.LikeListRepository;
import com.dev.moyering.user.dto.UserDto;
import com.dev.moyering.user.entity.User;
import com.dev.moyering.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final LikeListRepository likeListRepository;
    private final AlarmService alarmService;

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
                .ifPresent(f -> {
                    throw new RuntimeException("이미 팔로우 중입니다.");
                });

        Follow follow = Follow.builder()
                .follower(follower)
                .following(following)
                .build();
        Follow saved = followRepository.save(follow);
        System.out.println("수신자 : "+followingId +"발신자 : "+followerId);
        AlarmDto alarmDto = AlarmDto.builder()
                .alarmType(4)// '1: 시스템,관리자 알람 2 : 클래스링 알람, 3 : 게더링 알람, 4: 소셜링 알람',
                .title("팔로우 안내") // 필수 사항
                .receiverId(followingId)
                //수신자 유저 아이디
                .senderId(followerId)
                //발신자 유저 아이디
                .senderNickname(follower.getNickName())
                //발신자 닉네임 => 시스템/관리자가 발송하는 알람이면 메니저 혹은 관리자, 강사가 발송하는 알람이면 강사테이블의 닉네임, 그 외에는 유저 테이블의 닉네임(마이페이지 알림 내역에서 보낸 사람으로 보여질 이름)
                .content(follower.getNickName()+"님이 팔로우 하였습니다")//알림 내용
                .build();
        System.out.println("알람 보내기 테스트 " + alarmDto);
        alarmService.sendAlarm(alarmDto);

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
    public List<UserDto> getFollowers(Integer followingId, Integer page, Integer size, String search) {
        /*List<FollowDto> list = followRepository.findAllByFollowingUserId(followingId).stream()
                .map(Follow::toDto)
                .collect(Collectors.toList());

        // 검색 필터
        if (search != null && !search.trim().isEmpty()) {
            list = list.stream()
                    .filter(f -> {
                        User user = userRepository.findById(f.getFollowerId()).orElse(null);
                        return user != null && user.getNickName().toLowerCase().contains(search.toLowerCase());
                    })
                    .collect(Collectors.toList());
        }

        // 페이징 처리 (page * size ~ page * size + size)
        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, list.size());

        if (fromIndex >= list.size()) {
            return Collections.emptyList();
        }


        List<UserDto> userList = new ArrayList<>();
        for (FollowDto followDto : list.subList(fromIndex, toIndex)) {
            userList.add(userRepository.findById(followDto.getFollowerId()).get().toDto());
        }
        return userList;*/
        int offset = page * size;
//        System.out.println("DB 호출: offset=" + offset + ", size=" + size + ", search=" + search);

        return followRepository.findFollowersWithPaging(followingId, offset, size, search);
    }

    @Override
    public List<UserDto> getFollowings(Integer followerId, Integer pagem, Integer size, String search) {
        int offset = pagem * size;
        return followRepository.findFollowingsWithPaging(followerId, offset, size, search);
    }

    @Override
    public List<Integer> getFollowingUserIds(Integer followerId) {
        return followRepository.findFollowingIdsByFollowerId(followerId);
    }
}
