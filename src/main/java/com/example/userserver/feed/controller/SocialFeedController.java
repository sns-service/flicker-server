package com.example.userserver.feed.controller;

import com.example.userserver.auth.util.SecurityContextHolderUtils;
import com.example.userserver.feed.dto.*;
import com.example.userserver.feed.service.SocialFeedService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/feeds")
@RequiredArgsConstructor
public class SocialFeedController {

    private final SocialFeedService feedService;

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
        return feedService.getRandomFeeds();
    }

    @GetMapping("/user/{userId}")
    public List<FeedResponse> getUserFeeds(@PathVariable("userId") int userId) {
        return feedService.getAllFeedsByUploaderId(userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FeedResponse> getFeedById(@PathVariable("id") int id) {
        FeedResponse result = feedService.getFeedById(id);

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

    @GetMapping("/like/{postId}")
    public LikeResponse likePost(@PathVariable("postId") int postId, HttpServletRequest request) {
        boolean isLike = feedService.likePost(SecurityContextHolderUtils.getUserId(), postId);
        int count = feedService.countLike(postId);
        return new LikeResponse(count, isLike);
    }
}
