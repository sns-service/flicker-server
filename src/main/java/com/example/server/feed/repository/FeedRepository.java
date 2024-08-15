package com.example.server.feed.repository;

import com.example.server.feed.dto.FeedInfo;
import com.example.server.feed.entity.SocialFeed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    List<SocialFeed> findFeedsByUploaderId(@Param("uploaderId") int uploaderId);
    int likePost(int userId, int postId);
    int unlikePost(int userId, int postId);
    Boolean isLikePost(int userId, int postId);
    int countLikes(int postId);
    Page<SocialFeed> findAll(Pageable pageable);
    List<SocialFeed> findFeedsByFollowingsAfter(@Param("followingIds") List<Integer> followingIds, @Param("lastSeenId") int lastSeenId, Pageable pageable);
    List<SocialFeed> findInitialFeedsByFollowings(@Param("followingIds") List<Integer> followingIds, Pageable pageable);
    Page<SocialFeed> findAllWithUser(Pageable pageable);
}
