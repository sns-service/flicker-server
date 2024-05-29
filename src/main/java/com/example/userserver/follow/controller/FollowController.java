package com.example.userserver.follow.controller;

import com.example.userserver.follow.dto.FollowInfo;
import com.example.userserver.follow.dto.FollowRequest;
import com.example.userserver.follow.service.FollowService;
import com.example.userserver.user.dto.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.userserver.auth.util.SecurityContextHolderUtils.getUserId;

@RestController
@RequestMapping("/api/follows")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @GetMapping("/followers")
    public List<UserInfo> listFollowers() {
        return followService.listFollower(getUserId());
    }

    @GetMapping("/followings")
    public List<UserInfo> listFollowings() {
        return followService.listFollowing(getUserId());
    }

    @GetMapping("/follow/{userId}")
    public boolean isFollow(@PathVariable("userId") int userId) {
        return followService.isFollow(userId, getUserId());
    }

    @PostMapping("/follow")
    public FollowInfo followUser(@RequestBody FollowRequest followRequest) {
        int followerId = getUserId();
        return followService.followUser(followRequest.getUserId(), followerId);
    }

    @PostMapping("/unfollow")
    public Boolean unfollowUser(@RequestBody FollowRequest unfollowRequest) {
        int followerId = getUserId();
        return followService.unfollowUser(unfollowRequest.getUserId(), followerId);
    }
}
