package com.example.server.timeline.service;

import com.example.server.feed.dto.SocialPost;
import com.example.server.feed.entity.SocialFeed;
import com.example.server.feed.repository.FeedRepository;
import com.example.server.follow.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

        Pageable pageable = PageRequest.of(0, limit);

        if (lastSeenId == null) {
            feeds = feedRepository.findInitialFeedsByFollowings(followingIds, pageable);
        } else {  // Cursor 기반 추가 피드 조회
            feeds = feedRepository.findFeedsByFollowingsAfter(followingIds, lastSeenId, pageable);
        }

        return feeds.stream()
                .map(feed -> new SocialPost(
                        feed, feedRepository.countLikes(feed.getFeedId())))
                .collect(Collectors.toList());
    }
}
