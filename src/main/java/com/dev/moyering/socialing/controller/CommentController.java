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
@RequestMapping("/user/socialing")
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

    @PostMapping("/feed/comment")
    public ResponseEntity<CommentDto> postComment(
            @RequestBody CommentDto commentDto,
            @AuthenticationPrincipal PrincipalDetails principal) {

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Integer userId = principal.getUser().getUserId();
        try {
            CommentDto saved = commentService.addComment(commentDto.getFeedId(), userId,
                    commentDto.getContent(), commentDto.getParentId());
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }
}