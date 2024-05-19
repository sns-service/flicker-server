package com.example.userserver.follow.service;

import com.example.userserver.exception.BadRequestException;
import com.example.userserver.follow.entity.Follow;
import com.example.userserver.follow.repository.FollowRepository;
import com.example.userserver.user.dto.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;

    public boolean isFollow(int userId, int followerId) {
        return followRepository.findByUserIdAndFollowerId(userId, followerId).isPresent();
    }

    public Follow followUser(int userId, int followerId) {
        if (isFollow(userId, followerId)) {  // 이미 팔로우 한 경우에는, 더 이상 팔로우 할 것이 없다.
            return null;
        }

        return followRepository.save(new Follow(userId, followerId));
    }

    @Transactional
    public boolean unfollowUser(int userId, int followerId) {
        Follow follow = followRepository.findByUserIdAndFollowerId(userId, followerId)
                .orElseThrow(() -> new BadRequestException());

        followRepository.delete(follow);
        return true;
    }

    /**
     * 나를 팔로우 하고 있는 사람들 (팔로워) 조회 */
    public List<UserInfo> listFollower(int userId) {
        return followRepository.findFollowersByUserId(userId);
    }

    /**
     * 내가 팔로우 하는 사람들 (팔로잉) 조회 */
    public List<UserInfo> listFollowing(int userId) {
        return followRepository.findFollowingByUserId(userId);
    }
}
