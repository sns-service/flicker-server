package com.example.server.follow.service;

import com.example.server.exception.BadRequestException;
import com.example.server.follow.repository.FollowRepository;
import com.example.server.user.dto.UserInfo;
import com.example.server.user.entity.User;
import com.example.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public boolean isFollow(int followerId, int followingId) {
        return followRepository.isFollowing(followerId, followingId);
    }

    @Transactional
    public void followUser(int followerId, int followingId) {
        if (followerId == followingId) {
            throw new BadRequestException();
        }
        if (followRepository.isFollowing(followerId, followingId)) {  // 이미 팔로우 한 경우에는, 더 이상 팔로우 할 것이 없다.
            return;
        }
        followRepository.followUser(followerId, followingId);
    }

    @Transactional
    public void unfollowUser(int followerId, int followingId) {
        if (followRepository.isFollowing(followerId, followingId)) {
            throw new BadRequestException();
        }
        followRepository.unfollowUser(followerId, followingId);
    }

    /**
     * 내 팔로워들 조회 */
    public List<UserInfo> listFollower(int userId) {
        List<Integer> followerIds = followRepository.getFollowerIds(userId);

        // 팔로워 ID 목록을 기반으로 사용자 정보를 한 번의 쿼리로 조회
        List<User> followers = userRepository.findUsersByIds(followerIds);

        return followers.stream()
                .map(UserInfo::new)
                .collect(Collectors.toList());
    }

    /**
     * 내가 팔로우 하는 사람들 (팔로잉) 조회 */
    public List<UserInfo> listFollowing(int userId) {
        List<Integer> followingIds = followRepository.getFollowingIds(userId);

        List<User> followings = userRepository.findUsersByIds(followingIds);

        return followings.stream()
                .map(UserInfo::new)
                .collect(Collectors.toList());
    }
}
