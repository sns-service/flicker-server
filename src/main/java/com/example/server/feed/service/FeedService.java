package com.example.server.feed.service;

import com.example.server.exception.BadRequestException;
import com.example.server.feed.dto.CreateFeedRequest;
import com.example.server.feed.dto.FeedInfo;
import com.example.server.feed.dto.FeedResponse;
import com.example.server.feed.dto.SocialPost;
import com.example.server.feed.entity.SocialFeed;
import com.example.server.feed.repository.FeedJpaRepository;
import com.example.server.feed.repository.FeedRepository;
import com.example.server.user.entity.User;
import com.example.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final FeedRepository feedRepository;
    private final FeedJpaRepository feedJpaRepository;
    private final UserRepository userRepository;

    public List<SocialPost> getAllFeedsByUploaderId(int uploaderId) {
        List<SocialFeed> feeds = feedRepository.findFeedsByUploaderId(uploaderId);
        return convertToSocialPost(feeds);
    }

    public SocialPost getFeedById(int feedId) {
        SocialFeed feed = feedRepository.findById(feedId).orElse(null);
        return new SocialPost(feed, feedRepository.countLikes(feed.getFeedId()));
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

    private List<SocialPost> convertToSocialPost(List<SocialFeed> feeds) {
        return feeds.stream()
                .map(feed -> new SocialPost(feed, feedRepository.countLikes(feed.getFeedId())))
                .collect(Collectors.toList());
    }

    public List<SocialPost> getRandomFeedsByPaging() {
        int totalFeeds = (int) feedJpaRepository.count();
        int pageSize = 100;
        int totalPages = (totalFeeds + pageSize - 1) / pageSize;

        // 랜덤 페이지 선택
        Random random = new Random();
        int randomPageNumber = random.nextInt(totalPages);

        Pageable pageable = PageRequest.of(randomPageNumber, pageSize);
        Page<SocialFeed> feedPage = feedRepository.findAll(pageable);

        List<SocialFeed> feeds = new ArrayList<>(feedPage.getContent());
        Collections.shuffle(feeds);

        // 15개의 랜덤 피드 선택
        List<SocialFeed> randomFeeds = new ArrayList<>();
        for (int i = 0; i < Math.min(15, feeds.size()); i++) {
            randomFeeds.add(feeds.get(i));
        }

        List<FeedInfo> feedInfoList = randomFeeds.stream()
                .map(FeedInfo::new)
                .collect(Collectors.toList());

        return feedInfoList.stream()
                .map(feedInfo -> new SocialPost(feedInfo, feedRepository.countLikes(feedInfo.getFeedId())))
                .collect(Collectors.toList());
    }

    public boolean likePost(int userId, int feedId) {
        feedRepository.findById(feedId).orElseThrow(() -> new BadRequestException());

        if (feedRepository.isLikePost(userId, feedId)) {
            feedRepository.unlikePost(userId, feedId);
            return false;
        } else {
            feedRepository.likePost(userId, feedId);
            return true;
        }
    }

    public int countLike(int postId) {
        return feedRepository.countLikes(postId);
    }
}
