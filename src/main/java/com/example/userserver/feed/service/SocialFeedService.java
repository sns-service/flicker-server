package com.example.userserver.feed.service;

import com.example.userserver.exception.BadRequestException;
import com.example.userserver.feed.dto.CreateFeedRequest;
import com.example.userserver.feed.dto.FeedInfo;
import com.example.userserver.feed.dto.FeedResponse;
import com.example.userserver.feed.dto.SocialPost;
import com.example.userserver.feed.entity.SocialFeed;
import com.example.userserver.feed.repository.FeedRepository;
import com.example.userserver.user.entity.User;
import com.example.userserver.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SocialFeedService {

    private final FeedRepository feedRepository;
    private final UserRepository userRepository;

    public List<FeedInfo> getAllFeeds() {
        return feedRepository.findAllFeeds();
    }

    public List<FeedResponse> getAllFeedsByUploaderId(int uploaderId) {
        return feedRepository.findFeedsInfoByUploaderId(uploaderId);
    }

    public FeedResponse getFeedById(int feedId) {
        SocialFeed socialFeed = feedRepository.findById(feedId).orElse(null);
        return convertToSocialFeedResponse(socialFeed);
    }

    @Transactional
    public FeedResponse createFeed(CreateFeedRequest feedRequest, int userId) {
        if (feedRequest.getUploaderId() != userId) {
            throw new BadRequestException();
        }
        SocialFeed socialFeed = convertToSocialFeed(feedRequest);
        feedRepository.save(socialFeed);

        return convertToSocialFeedResponse(socialFeed);
    }

    public void deleteFeed(int feedId, int userId) {
        SocialFeed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new BadRequestException());

        if (feed.getUser().getUserId() != userId) {
            throw new BadRequestException();
        }

        feedRepository.delete(feed);
    }

    private SocialFeed convertToSocialFeed(CreateFeedRequest feedRequest) {
        User user = userRepository.findById(feedRequest.getUploaderId())
                .orElseThrow(() -> new BadRequestException());

        return SocialFeed.builder()
                .imageId(feedRequest.getImageId())
                .user(user)
                .contents(feedRequest.getContents())
                .build();
    }

    private FeedResponse convertToSocialFeedResponse(SocialFeed socialFeed) {
        return FeedResponse.builder()
                .feedId(socialFeed.getFeedId())
                .imageId(socialFeed.getImageId())
                .uploaderId(socialFeed.getUser().getUserId())
                .uploadDatetime(socialFeed.getUploadDatetime())
                .contents(socialFeed.getContents())
                .build();
    }

    public List<SocialPost> listAllFeed() {
        List<FeedInfo> feedList = feedRepository.findAllFeeds();

        return feedList.stream().map(
                feedInfo -> new SocialPost(feedInfo, feedRepository.countLikes(feedInfo.getFeedId()))
        ).toList();
    }

    public List<SocialPost> getRandomFeeds() {
        List<SocialPost> postList = listAllFeed();
        List<Integer> indices = new ArrayList<>();

        for (int i = 0; i < postList.size(); i++) {
            indices.add(i);
        }

        Collections.shuffle(indices, new Random());
        List<SocialPost> randomPosts = new ArrayList<>();

        for (int i = 0; i < 15; i++) {
            randomPosts.add(postList.get(indices.get(i)));
        }

        return randomPosts;
    }

    public boolean likePost(int userId, int postId) {
        if (feedRepository.isLikePost(userId, postId)) {
            feedRepository.unlikePost(userId, postId);
            return false;
        } else {
            feedRepository.likePost(userId, postId);
            return true;
        }
    }

    public int countLike(int postId) {
        return feedRepository.countLikes(postId);
    }
}
