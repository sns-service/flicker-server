package com.example.server.feed.controller;

import com.example.server.auth.util.SecurityContextHolderUtils;
import com.example.server.feed.dto.CreateFeedRequest;
import com.example.server.feed.dto.FeedResponse;
import com.example.server.feed.dto.SocialPost;
import com.example.server.feed.service.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feeds")
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;

    @GetMapping("/random")
    public List<SocialPost> listRandomFeeds() {
        return feedService.getRandomFeedsByPaging();
    }

    @GetMapping
    public List<SocialPost> getUserFeeds(@RequestParam("userId") int userId) {
        return feedService.getAllFeedsByUploaderId(userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SocialPost> getFeedById(@PathVariable("id") int id) {
        SocialPost result = feedService.getFeedById(id);

        if (result == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public FeedResponse createFeed(@RequestBody CreateFeedRequest feedRequest) {
        return feedService.createFeed(feedRequest, SecurityContextHolderUtils.getUserId());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeed(@PathVariable("id") int id) {
        feedService.deleteFeed(id, SecurityContextHolderUtils.getUserId());
        return ResponseEntity.ok().build();
    }
}
