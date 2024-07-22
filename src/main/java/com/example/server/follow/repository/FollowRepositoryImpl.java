package com.example.server.follow.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class FollowRepositoryImpl implements FollowRepository {

    private final FollowJpaRepository followJpaRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void followUser(int followerId, int followingId) {
        redisTemplate.opsForSet().add(followerId + ":following", followingId);
        redisTemplate.opsForSet().add(followingId + ":follower", followerId);
    }

    @Override
    public void unfollowUser(int followerId, int followingId) {
        redisTemplate.opsForSet().remove(followerId + ":following", followingId);
        redisTemplate.opsForSet().remove(followingId + ":follower", followerId);
    }

    @Override
    public boolean isFollowing(int followerId, int followingId) {
        Boolean isFollow = redisTemplate.opsForSet().isMember(followingId + ":follower", followerId);
        return isFollow != null && isFollow;
    }

    @Override
    public List<Integer> getFollowerIds(int userId) {
        Set<Object> followers = redisTemplate.opsForSet().members(userId + ":follower");
        if (followers != null) {
            return followers.stream().map(Object::toString).map(Integer::valueOf).collect(Collectors.toList());
        }
        return List.of();
    }

    @Override
    public List<Integer> getFollowingIds(int userId) {
        Set<Object> following = redisTemplate.opsForSet().members(userId + ":following");
        if (following != null) {
            return following.stream().map(Object::toString).map(Integer::valueOf).collect(Collectors.toList());
        }
        return List.of();
    }

    @Override
    public Integer getFollowerCount(int userId) {
        return Math.toIntExact(redisTemplate.opsForSet().size(userId + ":follower"));
    }

    @Override
    public Integer getFollowingCount(int userId) {
        return Math.toIntExact(redisTemplate.opsForSet().size(userId + ":following"));
    }
}
