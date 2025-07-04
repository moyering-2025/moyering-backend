package com.dev.moyering.socialing.service;

import com.dev.moyering.socialing.dto.CommentDto;
import com.dev.moyering.socialing.dto.FeedDto;
import com.dev.moyering.socialing.dto.LikeListDto;
import com.dev.moyering.socialing.entity.Comment;
import com.dev.moyering.socialing.entity.Feed;
import com.dev.moyering.socialing.entity.LikeList;
import com.dev.moyering.socialing.repository.CommentRepository;
import com.dev.moyering.socialing.repository.FeedRepository;
import com.dev.moyering.socialing.repository.LikeListRepository;
import com.dev.moyering.user.entity.User;
import com.dev.moyering.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedServiceImpl implements FeedService {

    private final FeedRepository feedRepository;
    private final CommentRepository commentRepository;
    private final LikeListRepository likeListRepository;
    private final UserRepository userRepository;

    private final EntityManager entityManager;

    @Value("${iupload.path}")
    private String iuploadPath;

    // 피드 전체
    @Override
    @Transactional
    public List<FeedDto> getFeeds(String sortType, Integer userId) throws Exception {

        List<FeedDto> feeds = feedRepository.findFeedsWithoutLiked(sortType);

        if (userId != null) {
            // 로그인한 경우 좋아요한 피드 id 리스트를 가져와서
            List<Integer> likedFeedIds = likeListRepository.findFeedIdsByUserId(userId);
            Set<Integer> likedFeedIdSet = new HashSet<>(likedFeedIds);

            System.out.println("========== Feed List ==========");
            for (FeedDto feed : feeds) {
                System.out.println("feedId: " + feed.getFeedId());
            }

            System.out.println("========== Liked Feed Ids ==========");
            System.out.println("▶▶ likedFeedIdSet = " + likedFeedIdSet);

            // DTO에 likedByUser 설정
            for (FeedDto feed : feeds) {
                feed.setLikedByUser(likedFeedIdSet.contains(feed.getFeedId()));
                feed.setMine(Objects.equals(feed.getWriterUserId(), userId));
            }
            for (FeedDto feed : feeds) {
                log.info("▶ feedId={}, writerUserId={}, userId={}, mine={}",
                        feed.getFeedId(),
                        feed.getWriterUserId(),
                        userId,
                        feed.getMine());
            }

        } else {
            // 비로그인 사용자라면 likedByUser = false
            for (FeedDto feed : feeds) {
                feed.setLikedByUser(false);
                feed.setMine(false);
            }
        }

        return feeds;
    }

    @Override
    @Transactional
    public FeedDto getFeedDetail(Integer feedId, Integer currentUserId) {
        // 1) Feed 조회
        Feed feed = feedRepository
                .findByFeedIdAndIsDeletedFalse(feedId)
                .orElseThrow(() -> new NoSuchElementException("Feed not found: " + feedId));

        // 2) FeedDto 기본 매핑
        FeedDto dto = feed.toDto();
        dto.setCreatedAt(feed.getCreateDate());
        dto.setMine(currentUserId != null &&
                feed.getUser().getUserId().equals(currentUserId));

        // 3) 좋아요/댓글 수, likedByUser
        dto.setLikesCount(likeListRepository.countByFeedFeedId(feedId));
        dto.setLikedByUser(currentUserId != null &&
                likeListRepository.existsByFeedFeedIdAndUserUserId(feedId, currentUserId)
        );
        dto.setCommentsCount(commentRepository.countByFeedFeedIdAndIsDeletedFalse(feedId));

        // 4) 댓글 + 대댓글 구성
        List<CommentDto> commentDtos = commentRepository
                .findByFeedFeedIdAndParentIdIsNullAndIsDeletedFalseOrderByCreateAtAsc(feedId)
                .stream()
                .map(parent -> {
                    CommentDto pd = parent.toDto();
                    List<CommentDto> replies = commentRepository
                            .findByParentIdAndIsDeletedFalseOrderByCreateAtAsc(parent.getCommentId())
                            .stream()
                            .map(Comment::toDto)
                            .collect(Collectors.toList());
                    pd.setReplies(replies);
                    return pd;
                })
                .collect(Collectors.toList());
        dto.setComments(commentDtos);

        // 5) 더 많은 게시물 썸네일(img1)
        Integer writerId = feed.getUser().getUserId();
        List<String> thumbs = feedRepository.findTop3Img1ByUserId(writerId);
        dto.setMoreImg1List(thumbs);

        return dto;
    }

//    @Override
//    public List<FeedDto> getFeedsByUserId(Integer userId) throws Exception {
//        List<Feed> feeds = feedRepository.findAllByUserUserIdOrderByCreateDateDesc(userId);
//        return feeds.stream().map(Feed::toDto).collect(Collectors.toList());
//    }

    @Override
    public List<FeedDto> getFeedsByNickname(String nickname, Integer userId) throws Exception {
        User user = userRepository.findByNickName(nickname).orElseThrow(() -> new Exception("유저를 찾을 수 없습니다 : " + nickname));
        List<Feed> feeds = feedRepository.findByUserNickName(nickname);

        return feeds.stream().map(feed -> {
                    FeedDto dto = feed.toDto();
                    dto.setCommentsCount(commentRepository.countByFeedFeedIdAndIsDeletedFalse(feed.getFeedId()));
                    dto.setLikesCount(likeListRepository.countByFeedFeedId(feed.getFeedId()));

                    dto.setCreatedAt(feed.getCreateDate());
                    dto.setMine(feed.getUser().getUserId().equals(userId));

                    List<CommentDto> commentDtos = commentRepository.findByFeed_FeedIdOrderByCreateAtAsc(feed.getFeedId())
                            .stream().map(Comment::toDto)
                            .collect(Collectors.toList());
                    dto.setComments(commentDtos);

                    List<String> moreImgs = feeds.stream()
                            .filter(f -> !f.getFeedId().equals(feed.getFeedId()))
                            .map(Feed::getImg1)
                            .collect(Collectors.toList());
                    dto.setMoreImg1List(moreImgs);

                    return dto;
                })
                .collect(Collectors.toList());

    }

    @Override
    public Map<String, Object> getFeedsByUserId(Integer userId) throws Exception {
        List<Feed> feedList = feedRepository.findByUserUserId(userId);
        List<FeedDto> dtoList = new ArrayList<>();
        List<LikeList> likeList = new ArrayList<>();
        List<LikeListDto> likeListDtos = new ArrayList<>();

        for (Feed feed : feedList) {
            dtoList.add(feed.toDto());
            likeList = likeListRepository.findByFeedFeedId(feed.getFeedId());
        }
        for (LikeList like : likeList) {
            likeListDtos.add(like.toDto());
        }
        Map<String, Object> map = new HashMap<>();
        map.put("likeList", likeListDtos);
        map.put("feedList", dtoList);

        return map;
    }

    @Override
    @Transactional
    public Integer createFeed(FeedDto feedDto, List<MultipartFile> images) throws Exception {
        File uploadDir = new File(iuploadPath);
        if (!uploadDir.exists()) {
            boolean created = uploadDir.mkdirs();
            if (!created) {
                throw new IOException("업로드 디렉터리 생성에 실패했습니다: " + uploadDir.getAbsolutePath());
            }
        }
//        System.out.println("uploadDir.exists=" + uploadDir.exists()
//                + ", canRead=" + uploadDir.canRead()
//                + ", canWrite=" + uploadDir.canWrite());

        User writer = userRepository.findByNickName(feedDto.getWriterId())
                .orElseThrow(() -> new Exception("작성자 '" + feedDto.getWriterId() + "'를 찾을 수 없습니다."));

        Feed feed = feedDto.toEntity();
        feed.setUser(writer);
        if (images != null) {                                                          // 나중에 이미지 중복체크 넣기
            for (int i = 0; i < images.size() && i < 5; i++) {
                MultipartFile img = images.get(i);
                if (!img.isEmpty()) {
                    String filename = img.getOriginalFilename();
                    File file = new File(iuploadPath, filename);
                    img.transferTo(file);

                    String publicUrl = filename;
                    switch (i) {
                        case 0:
                            feed.setImg1(publicUrl);
                            break;
                        case 1:
                            feed.setImg2(publicUrl);
                            break;
                        case 2:
                            feed.setImg3(publicUrl);
                            break;
                        case 3:
                            feed.setImg4(publicUrl);
                            break;
                        case 4:
                            feed.setImg5(publicUrl);
                            break;
                    }
                }
            }
        }

        feedRepository.save(feed);
        Integer feedNum = feed.getFeedId();
        entityManager.clear();
        return feedNum;
    }

    @Override
    @Transactional
    public void updateFeed(Integer feedId
//            , FeedDto feedDto
            , String text, List<String> tags
            , List<MultipartFile> images
//            , List<String> removeUrls
    ) throws Exception {
        Feed feed = feedRepository.findById(feedId).orElseThrow(() -> new Exception("수정하려고 하는 피드가 없습니다"));

        /*feed.setContent(feedDto.getContent());
        feed.setTag1(feedDto.getTag1());
        feed.setTag2(feedDto.getTag2());
        feed.setTag3(feedDto.getTag3());
        feed.setTag4(feedDto.getTag4());
        feed.setTag5(feedDto.getTag5());*/

        if (tags.size() > 0) feed.setTag1(tags.get(0));
        if (tags.size() > 1 ) feed.setTag2(tags.get(1));
        if (tags.size() > 2 ) feed.setTag3(tags.get(2));
        if (tags.size() > 3 ) feed.setTag4(tags.get(3));
        if (tags.size() > 4 ) feed.setTag5(tags.get(4));

        /*List<String> currentUrls = new ArrayList<>();
        if (feed.getImg1() != null) currentUrls.add(feed.getImg1());
        if (feed.getImg2() != null) currentUrls.add(feed.getImg2());
        if (feed.getImg3() != null) currentUrls.add(feed.getImg3());
        if (feed.getImg4() != null) currentUrls.add(feed.getImg4());
        if (feed.getImg5() != null) currentUrls.add(feed.getImg5());*/

//        if (images.get(0) != null ) feed.setImg1(images.get(0).getOriginalFilename());
//        if (images.get(1) != null) feed.setImg2(images.get(1).getOriginalFilename());
//        if (images.get(2) != null ) feed.setImg3(images.get(2).getOriginalFilename());
//        if (images.get(3) != null && images.get(3).isEmpty()) feed.setImg4(images.get(3).getOriginalFilename());
//        if (images.get(4) != null && images.get(4).isEmpty()) feed.setImg5(images.get(4).getOriginalFilename());
//        if (images.size() > 0) feed.setImg1(images.get(0).getOriginalFilename());
//        if (images.size() > 1) feed.setImg2(images.get(1).getOriginalFilename());
//        if (images.size() > 2) feed.setImg3(images.get(2).getOriginalFilename());
//        if (images.size() > 3) feed.setImg4(images.get(3).getOriginalFilename());
//        if (images.size() > 4) feed.setImg5(images.get(4).getOriginalFilename());
        feed.setImg1(images.size() > 0 ? images.get(0).getOriginalFilename() : null);
        feed.setImg2(images.size() > 1 ? images.get(1).getOriginalFilename() : null);
        feed.setImg3(images.size() > 2 ? images.get(2).getOriginalFilename() : null);
        feed.setImg4(images.size() > 3 ? images.get(3).getOriginalFilename() : null);
        feed.setImg5(images.size() > 4 ? images.get(4).getOriginalFilename() : null);


        if (text!=null) feed.setContent(text);

        Path dir = Paths.get(iuploadPath);
        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        }

        /*// 삭제 요청된 파일 제거
        if (removeUrls != null && !removeUrls.isEmpty()) {
            for (String url : removeUrls) {
                String filename = Paths.get(url).getFileName().toString();
                try {
                    Files.deleteIfExists(dir.resolve(filename));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // currentUrls 에서 제거
            currentUrls = currentUrls.stream()
                    .filter(u -> !removeUrls.contains(u))
                    .collect(Collectors.toList());
        }
*/
        /*// 새로운 이미지 저장
        if (images != null && !images.isEmpty()) {
            for (MultipartFile img : images) {
                if (img.isEmpty()) continue;
                String orig = img.getOriginalFilename();
                String fname = System.currentTimeMillis() + "_" + orig;
                Path target = dir.resolve(fname);

                try (InputStream is = img.getInputStream()) {
                    Files.copy(is, target, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException("이미지 저장 실패: " + fname, e);
                }

                // DB에는 파일명만 넣어둠
                currentUrls.add(fname);
            }
        }

        // 엔티티 이미지 필드 초기화
        feed.setImg1(null);
        feed.setImg2(null);
        feed.setImg3(null);
        feed.setImg4(null);
        feed.setImg5(null);

        // 최대 5개 까지 세팅
        if (currentUrls.size() > 0) feed.setImg1(currentUrls.get(0));
        if (currentUrls.size() > 1) feed.setImg2(currentUrls.get(1));
        if (currentUrls.size() > 2) feed.setImg3(currentUrls.get(2));
        if (currentUrls.size() > 3) feed.setImg4(currentUrls.get(3));
        if (currentUrls.size() > 4) feed.setImg5(currentUrls.get(4));*/

        feedRepository.save(feed);
    }

    @Override
    public boolean isLikedByUser(Integer feedId, Integer userId) {
        return likeListRepository.existsByFeedFeedIdAndUserUserId(feedId, userId);
    }

    @Override
    public List<FeedDto> getPopularFeeds(Integer page, Integer size) throws Exception {
        if (page == null || page < 0) page = 0;
        if (size == null || size <= 0) size = 10;

        int offset = page * size;
        return feedRepository.findTopLikedFeeds(offset, size);
    }


}


