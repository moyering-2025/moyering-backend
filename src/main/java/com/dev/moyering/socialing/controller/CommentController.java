package com.dev.moyering.socialing.controller;

import com.dev.moyering.auth.PrincipalDetails;
import com.dev.moyering.socialing.dto.CommentDto;
import com.dev.moyering.socialing.service.CommentService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/socialing")
public class CommentController {

    private final CommentService commentService;

//    @PostMapping
//    public ResponseEntity<?> addComment(@RequestBody CommentDto dto) {
//        try {
//            commentService.saveComment(dto);
//            return ResponseEntity.ok().build();
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Integer commentId, @RequestParam Integer userId) {
        try {
            commentService.deleteComment(commentId, userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/feed/{feedId}/comment")
    public ResponseEntity<CommentDto> postComment(
            @PathVariable Integer feedId,
            @RequestBody CommentRequest request,
            @AuthenticationPrincipal PrincipalDetails principal) {

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Integer userId = principal.getUser().getUserId();
        try {
            CommentDto saved = commentService.addComment(feedId, userId,
                    request.getContent(), request.getParentId());
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @Data()
    public static class CommentRequest {
        private String content;
        private Integer parentId;  // null 이면 최상위 댓글, 값이 있으면 대댓글
    }
}