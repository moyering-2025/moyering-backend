package com.dev.moyering.socialing.controller;

import com.dev.moyering.auth.PrincipalDetails;
import com.dev.moyering.socialing.dto.ScrapDto;
import com.dev.moyering.socialing.dto.ScrapListDto;
import com.dev.moyering.socialing.service.ScrapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/socialing/scrap")
public class ScrapController {

    private final ScrapService scrapService;

    @PostMapping()
    public ResponseEntity<ScrapDto> createScrap(@AuthenticationPrincipal PrincipalDetails principal,
                                                @RequestParam Integer feedId) throws Exception {
        Integer userId = principal.getUser().getUserId();
        ScrapDto scrapDto = scrapService.createScrap(userId, feedId);
        return ResponseEntity.status(HttpStatus.CREATED).body(scrapDto);
    }

    @DeleteMapping("/{feedId}")
    public ResponseEntity<Void> deleteScrap(
            @AuthenticationPrincipal PrincipalDetails principal,
            @PathVariable Integer feedId
    ) throws Exception {
        Integer userId = principal.getUser().getUserId();
        scrapService.deleteScrap(userId, feedId);
        return ResponseEntity.noContent().build();
    }

//    @GetMapping
//    public ResponseEntity<List<Integer>> getScrapFeedIds(
//            @AuthenticationPrincipal PrincipalDetails principal
//    ) {
//        Integer userId = principal.getUser().getUserId();
//        List<Integer> feedIds = scrapService.getScrapFeedIds(userId);
//        return ResponseEntity.ok(feedIds);
//    }

    @GetMapping("/{feedId}")
    public ResponseEntity<Boolean> isScrapped(
            @AuthenticationPrincipal PrincipalDetails principal,
            @PathVariable Integer feedId
    ) {
        Integer userId = principal.getUser().getUserId();
        boolean scrapped = scrapService.isScrapped(userId, feedId);
        return ResponseEntity.ok(scrapped);
    }

    @GetMapping
    public ResponseEntity<List<Integer>> getScrapFeedIds(
            @AuthenticationPrincipal PrincipalDetails principal
    ) {
        Integer userId = principal.getUser().getUserId();
        List<Integer> feedIds = scrapService.getScrapFeedIds(userId);
        return ResponseEntity.ok(feedIds); // [1,2,5,7] 이런 feedId 리스트
    }

    @GetMapping("/myScraps")
    public ResponseEntity<List<ScrapListDto>> getMyScrapsCursor(
            @AuthenticationPrincipal PrincipalDetails principal,
            @RequestParam(required = false) Integer lastScrapId,
            @RequestParam(defaultValue = "12") int size) {

        Integer userId = principal.getUser().getUserId();
        List<ScrapListDto> scraps = scrapService.getMyScrapsCursor(userId, lastScrapId, size);
        return ResponseEntity.ok(scraps);
    }
}
