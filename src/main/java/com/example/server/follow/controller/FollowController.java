package com.example.server.follow.controller;

import com.example.server.auth.util.SecurityContextHolderUtils;
import com.example.server.follow.dto.FollowRequest;
import com.example.server.user.dto.UserInfo;
import com.example.server.follow.dto.FollowInfo;
import com.example.server.follow.service.FollowService;
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
        return followService.isFollow(userId, SecurityContextHolderUtils.getUserId());
    }

    @PostMapping("/follow")
    public FollowInfo followUser(@RequestBody FollowRequest followRequest) {
        int followerId = SecurityContextHolderUtils.getUserId();
        return followService.followUser(followRequest.getUserId(), followerId);
    }

    @PostMapping("/unfollow")
    public Boolean unfollowUser(@RequestBody FollowRequest unfollowRequest) {
        int followerId = SecurityContextHolderUtils.getUserId();
        return followService.unfollowUser(unfollowRequest.getUserId(), followerId);
    }
}
