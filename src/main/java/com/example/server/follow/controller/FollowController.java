package com.example.server.follow.controller;

import com.example.server.auth.util.SecurityContextHolderUtils;
import com.example.server.follow.dto.FollowRequest;
import com.example.server.follow.service.FollowService;
import com.example.server.user.dto.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/follows")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @GetMapping("/followers")
    public List<UserInfo> listFollowers() {
        return followService.listFollower(SecurityContextHolderUtils.getUserId());
    }

    @GetMapping("/followings")
    public List<UserInfo> listFollowings() {
        return followService.listFollowing(SecurityContextHolderUtils.getUserId());
    }

    @GetMapping("/follow/{userId}")
    public boolean isFollow(@PathVariable("userId") int userId) {
        return followService.isFollow(SecurityContextHolderUtils.getUserId(), userId);
    }

    @PostMapping("/follow")
    public void followUser(@RequestBody FollowRequest followRequest) {
        int followerId = SecurityContextHolderUtils.getUserId();
        followService.followUser(followerId, followRequest.getUserId());
    }

    @PostMapping("/unfollow")
    public void unfollowUser(@RequestBody FollowRequest unfollowRequest) {
        int followerId = SecurityContextHolderUtils.getUserId();
        followService.unfollowUser(followerId, unfollowRequest.getUserId());
    }
}
