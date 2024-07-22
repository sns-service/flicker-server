package com.example.server.feed.repository;

import com.example.server.feed.dto.FeedInfo;
import com.example.server.feed.dto.FeedResponse;
import com.example.server.feed.entity.SocialFeed;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FeedRepositoryImpl implements FeedRepository {

    private final FeedJpaRepository feedJpaRepository;
    private final StringRedisTemplate redisTemplate;
    private final FeedPagingRepository feedPagingRepository;

    @Override
    public SocialFeed save(SocialFeed feed) {
        return feedJpaRepository.save(feed);
    }

    @Override
    public void delete(SocialFeed feed) {
        feedJpaRepository.delete(feed);
    }

    @Override
    public Optional<SocialFeed> findById(int feedId) {
        return feedJpaRepository.findById(feedId);
    }

    @Override
    public List<FeedInfo> findAllFeeds() {
        return feedJpaRepository.findAllFeeds();
    }

    @Override
    public List<FeedResponse> findFeedsInfoByUploaderId(int uploaderId) {
        return feedJpaRepository.findFeedsInfoByUploaderId(uploaderId);
    }

    @Override
    public int likePost(int userId, int postId) {
        return Math.toIntExact(redisTemplate.opsForSet().add("likes:" + postId, String.valueOf(userId)));
    }

    @Override
    public int unlikePost(int userId, int postId) {
        return Math.toIntExact(redisTemplate.opsForSet().remove("likes:" + postId, String.valueOf(userId)));
    }

    @Override
    public Boolean isLikePost(int userId, int postId) {
        return redisTemplate.opsForSet().isMember("likes:"+postId, String.valueOf(userId));
    }

    @Override
    public int countLikes(int postId) {
        return Math.toIntExact(redisTemplate.opsForSet().size("likes:" + postId));
    }

    @Override
    public Page<SocialFeed> findAll(Pageable pageable) {
        return feedPagingRepository.findAll(pageable);
    }

    @Override
    public List<SocialFeed> findFeedsByFollowersAfter(List<Integer> followerIds, int lastSeenId, int limit) {
        return feedJpaRepository.findFeedsByFollowersAfter(followerIds, lastSeenId, limit);
    }

    @Override
    public List<SocialFeed> findInitialFeedsByFollowers(List<Integer> followerIds, int limit) {
        return feedJpaRepository.findInitialFeedsByFollowers(followerIds, limit);
    }
}
