package com.example.server.feed.repository;

import com.example.server.feed.dto.FeedInfo;
import com.example.server.feed.entity.SocialFeed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FeedJpaRepository extends JpaRepository<SocialFeed, Integer> {

    @Query("SELECT new com.example.server.feed.dto.FeedInfo(f) FROM SocialFeed f")
    List<FeedInfo> findAllFeeds();

    @Query("SELECT f FROM SocialFeed f WHERE f.user.userId = :uploaderId")
    List<SocialFeed> findFeedsByUploaderId(@Param("uploaderId") int uploaderId);

    @Query(value = "SELECT * FROM social_feed WHERE user_id IN :followingIds AND feed_id < :lastSeenId ORDER BY feed_id DESC LIMIT :limit", nativeQuery = true)
    List<SocialFeed> findFeedsByFollowersAfter(@Param("followingIds") List<Integer> followerIds, @Param("lastSeenId") int lastSeenId, @Param("limit") int limit);

    @Query(value = "SELECT * FROM social_feed WHERE user_id IN :followerIds ORDER BY feed_id DESC LIMIT :limit", nativeQuery = true)
    List<SocialFeed> findInitialFeedsByFollowers(@Param("followerIds") List<Integer> followerIds, @Param("limit") int limit);
}