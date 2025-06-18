package com.dev.moyering.socialing.controller;

import com.dev.moyering.socialing.dto.FeedDto;
import com.dev.moyering.socialing.service.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/socialing")
public class FeedController {

    private final FeedService feedService;

    @GetMapping("/feeds")
    public ResponseEntity<List<FeedDto>> getFeeds(
            @RequestParam(defaultValue = "all") String sort,
            @RequestParam(required = false) String userId
    ) {

        try {
            List<FeedDto> feeds = feedService.getFeeds(sort, userId);
            return new ResponseEntity<>(feeds, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
