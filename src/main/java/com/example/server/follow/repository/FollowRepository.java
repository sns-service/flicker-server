package com.example.server.follow.repository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository {
    void followUser(int followerId, int followingId);
    void unfollowUser(int followerId, int followingId);
    boolean isFollowing(int followerId, int followingId);
    List<Integer> getFollowerIds(int userId);
    List<Integer> getFollowingIds(int userId);
    Integer getFollowerCount(int userId);
    Integer getFollowingCount(int userId);
}
