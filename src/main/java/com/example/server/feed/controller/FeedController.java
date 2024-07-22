package com.example.server.feed.controller;

import com.example.server.auth.util.SecurityContextHolderUtils;
import com.example.server.feed.dto.*;
import com.example.server.feed.service.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/feeds")
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;

    @GetMapping
    public List<FeedInfo> getAllFeeds() {
        List<FeedInfo> result = new ArrayList<>();

        for (FeedInfo feedInfo : feedService.getAllFeeds()) {
            result.add(feedInfo);
        }
        return result;
    }

    @GetMapping("/random")
    public List<SocialPost> listRandomFeeds() {
        return feedService.getRandomFeedsByPaging();
    }

    @GetMapping("/user/{userId}")
    public List<SocialPost> getUserFeeds(@PathVariable("userId") int userId) {
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
    public SocialPost createFeed(@RequestBody CreateFeedRequest feedRequest) {
        return feedService.createFeed(feedRequest, SecurityContextHolderUtils.getUserId());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeed(@PathVariable("id") int id) {
        feedService.deleteFeed(id, SecurityContextHolderUtils.getUserId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/like/{postId}")
    public LikeResponse likeFeed(@PathVariable("postId") int postId) {
        boolean isLike = feedService.likePost(SecurityContextHolderUtils.getUserId(), postId);
        int count = feedService.countLike(postId);
        return new LikeResponse(count, isLike);
    }
}
