package com.example.server.feed.repository;

import com.example.server.feed.dto.FeedInfo;
import com.example.server.feed.dto.FeedResponse;
import com.example.server.feed.entity.SocialFeed;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeedRepository {

    SocialFeed save(SocialFeed feed);
    void delete(SocialFeed feed);
    Optional<SocialFeed> findById(int feedId);
    List<FeedInfo> findAllFeeds();
    List<FeedResponse> findFeedsInfoByUploaderId(@Param("uploaderId") int uploaderId);
    int likePost(int userId, int postId);
    int unlikePost(int userId, int postId);
    Boolean isLikePost(int userId, int postId);
    int countLikes(int postId);
}
