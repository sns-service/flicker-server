package com.example.userserver.follow.controller;

import com.example.userserver.follow.dto.FollowInfo;
import com.example.userserver.follow.dto.FollowRequest;
import com.example.userserver.follow.service.FollowService;
import com.example.userserver.user.dto.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/follows")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @GetMapping("followers/{userId}")
    public List<UserInfo> listFollowers(@PathVariable("userId") int userId) {
        return followService.listFollower(userId);
    }

    @GetMapping("/followings/{userId}")
    public List<UserInfo> listFollowings(@PathVariable("userId") int userId) {
        return followService.listFollowing(userId);
    }

    @GetMapping("/follow/{userId}/{followerId}")
    public boolean isFollow(
            @PathVariable("userId") int userId,
            @PathVariable("followerId") int followerId
    ) {
        return followService.isFollow(userId, followerId);
    }

    @PostMapping("/follow")
    public FollowInfo followUser(@RequestBody FollowRequest followRequest) {
        return followService.followUser(followRequest.getUserId(), followRequest.getFollowerId());
    }

    @PostMapping("/unfollow")
    public Boolean unfollowUser(@RequestBody FollowRequest unfollowRequest) {
        return followService.unfollowUser(unfollowRequest.getUserId(), unfollowRequest.getFollowerId());
    }
}
