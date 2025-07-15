package com.dev.moyering.socialing.service;

import com.dev.moyering.admin.service.AdminBadgeScoreService;
import com.dev.moyering.common.dto.AlarmDto;
import com.dev.moyering.common.service.AlarmService;
import com.dev.moyering.socialing.dto.CommentDto;
import com.dev.moyering.socialing.entity.Comment;
import com.dev.moyering.socialing.entity.Feed;
import com.dev.moyering.socialing.repository.CommentRepository;
import com.dev.moyering.socialing.repository.FeedRepository;
import com.dev.moyering.user.entity.User;
import com.dev.moyering.user.repository.UserRepository;
import com.dev.moyering.user.service.UserBadgeService;
import com.dev.moyering.user.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final FeedRepository feedRepository;
    private final UserRepository userRepository;

	private final UserService userService;
	private final AdminBadgeScoreService adminBadgeScoreService;
	private final UserBadgeService userBadgeService;
	private final AlarmService alarmService;
	
    @Override
    public List<CommentDto> getComments(Integer feedId) {
    return null;
    }

    @Transactional
    @Override
    public void saveComment(CommentDto commentDto) throws Exception {
        Comment comment = commentDto.toEntity();
        
        Integer feedId = comment.getFeed().getFeedId();
        Feed feed = feedRepository.findById(feedId).get();
        
        
        
//        AlarmDto alarmDto = AlarmDto.builder()
//				.alarmType(2)// '1: 시스템,관리자 알람 2 : 클래스링 알람, 3 : 게더링 알람, 4: 소셜링 알람',
//				.title("댓글 알림") // 필수 사항
//				.receiverId(feed.getUser().getUserId())
//				//수신자 유저 아이디
//				.senderId(comment.getUser().getUserId())
//				//발신자 유저 아이디 
//				.senderNickname(feed.getUser().getNickName())
//				//발신자 닉네임 => 시스템/관리자가 발송하는 알람이면 메니저 혹은 관리자, 강사가 발송하는 알람이면 강사테이블의 닉네임, 그 외에는 유저 테이블의 닉네임(마이페이지 알림 내역에서 보낸 사람으로 보여질 이름)
//				.content(comment.getUser().getNickName()+"님께서 댓글을 남기셨습니다.")//알림 내용
//				.build();
//		alarmService.sendAlarm(alarmDto);
        
        commentRepository.save(comment);
    }

    @Override
    public void deleteComment(Integer commentId, Integer userId) throws Exception {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new Exception("댓글이 존재하지 않습니다."));
        if (!comment.getUser().getUserId().equals(userId)) {
            throw new Exception("댓글을 삭제할 권한이 없습니다.");
        }
        comment.setDeleted(true);
    }

    @Transactional
    @Override
    public CommentDto addComment(Integer feedId, Integer userId, String content, Integer parentId) throws Exception {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new NoSuchElementException("해당 피드를 찾을 수 없습니다. feedId=" + feedId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("해당 사용자를 찾을 수 없습니다. userId=" + userId));
        if (content == null || content.isBlank()) {
            throw new Exception("댓글 내용을 입력해주세요.");
        }
        Comment comment = Comment.builder()
                .feed(feed)
                .user(user)
                .content(content)
                .parentId(parentId)
                .isDeleted(false)
                .build();

        Comment saved = commentRepository.save(comment);
        
        //소셜링 댓글 작성 시 포인트 획득
        //증가시킬 포인트 찾기
        Integer score = adminBadgeScoreService.getScoreByTitle("소셜링 댓글 작성");
        //유저의 활동점수 증가
        userService.addScore(comment.getUser().getUserId(), score);
        //뱃지 획득 가능 여부 확인
        userBadgeService.giveBadgeWithScore(comment.getUser().getUserId());

        // ---------- parentWriterId 채우기 ----------
       /* CommentDto dto = saved.toDto();
        if (parentId != null) {
            Comment parent = commentRepository.findById(parentId)
                    .orElseThrow(() -> new NoSuchElementException("부모 댓글을 찾을 수 없습니다. parentId=" + parentId));
            dto.setParentWriterId(parent.getUser().getNickName());
        }
//        return saved.toDto();
        return dto;*/
        List<Comment> allComments = commentRepository.findByFeedFeedId(feedId);
        return saved.toDto(allComments);
    }
}
