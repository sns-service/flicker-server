package com.example.server.like.controller;

import com.example.server.auth.util.SecurityContextHolderUtils;
import com.example.server.like.dto.LikeResponse;
import com.example.server.feed.service.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/like")
@RequiredArgsConstructor
public class LikeController {

    private final FeedService feedService;

    @PostMapping
    public LikeResponse likePost(@RequestParam("feedId") int postId) {
        boolean isLike = feedService.likePost(SecurityContextHolderUtils.getUserId(), postId);
        int count = feedService.countLike(postId);
        return new LikeResponse(count, isLike);
    }
}
