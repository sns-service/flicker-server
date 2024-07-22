package com.example.server.timeline.service;

import com.example.server.feed.dto.SocialPost;
import com.example.server.feed.entity.SocialFeed;
import com.example.server.feed.repository.FeedRepository;
import com.example.server.follow.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TimelineService {

    private final FollowRepository followRepository;
    private final FeedRepository feedRepository;

    public List<SocialPost> getUserTimeline(int userId, Integer lastSeenId, int limit) {
        List<Integer> followingIds = followRepository.getFollowingIds(userId);
        List<SocialFeed> feeds;

        if (lastSeenId == null) {  // 초기 피드 조회
            feeds = feedRepository.findInitialFeedsByFollowers(followingIds, limit);
        } else {  // 커서 기반 추가 피드 조회
            feeds = feedRepository.findFeedsByFollowersAfter(followingIds, lastSeenId, limit);
        }

        return feeds.stream()
                .map(feed -> new SocialPost(
                        feed, feedRepository.countLikes(feed.getFeedId())))
                .collect(Collectors.toList());
    }
}
