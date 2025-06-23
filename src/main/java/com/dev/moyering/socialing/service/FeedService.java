package com.dev.moyering.socialing.service;

import com.dev.moyering.socialing.dto.CommentDto;
import com.dev.moyering.socialing.dto.FeedDto;
import com.dev.moyering.socialing.entity.Feed;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FeedService {

    // 전체 피드 목록 조회
    List<FeedDto> getFeeds(String sortType, String userId) throws Exception;

    // 피드 상세 조회
    FeedDto getFeedDetail(Integer feedId, Integer currentUserId);

    //    //usernickname의 모든 피드를 리스트로 조회
//    List<FeedDto> getFeedsByUserId(Integer userId) throws Exception;
    List<FeedDto> getFeedsByUserId(Integer userId) throws Exception;

    List<FeedDto> getFeedsByNickname(String nickname) throws Exception;


    // 피드 작성
    Integer createFeed(FeedDto feedDto, List<MultipartFile> images) throws Exception;
}
