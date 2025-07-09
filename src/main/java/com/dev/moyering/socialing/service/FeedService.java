package com.dev.moyering.socialing.service;

import com.dev.moyering.socialing.dto.CommentDto;
import com.dev.moyering.socialing.dto.FeedDto;
import com.dev.moyering.socialing.entity.Feed;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface FeedService {

    // 전체 피드 목록 조회
    List<FeedDto> getFeeds(String sortType, Integer userId) throws Exception;

    // 피드 상세 조회
    FeedDto getFeedDetail(Integer feedId, Integer currentUserId);


    /**
     * 주어진 nickName으로 해당 유저의 모든 피드를 가져옵니다.
     *
     * @param "nickname" 조회할 사용자의 닉네임
     * @param "userId"   현재 로그인된 사용자의 userId (좋아요 여부, mine 판단용)
     * @return FeedDto 리스트
     * @throws Exception
     */
    List<FeedDto> getFeedsByNickname(String nickname, Integer userId) throws Exception;

    Map<String, Object> getFeedsByUserId(Integer userId) throws Exception;

    // 피드 작성
    Integer createFeed(FeedDto feedDto, List<MultipartFile> images) throws Exception;

    void updateFeed(Integer feedId
//            , FeedDto feedDto
            , String text, String tag1, String tag2, String tag3, String tag4, String tag5, MultipartFile image1,
                    MultipartFile image2, MultipartFile image3, MultipartFile image4, MultipartFile image5
            , List<String> removeUrls
    ) throws Exception;

    boolean isLikedByUser(Integer feedId, Integer userId);

    List<FeedDto> getPopularFeeds(Integer page, Integer size) throws Exception;

    void deleteFeed(Integer feedId,Integer userId) throws Exception;
    
    Map<Integer,Integer> myFeedsLikeCount(Integer userId) throws Exception;
}
