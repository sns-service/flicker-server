package com.example.server.feed.repository;

import com.example.server.feed.dto.FeedInfo;
import com.example.server.feed.entity.SocialFeed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FeedJpaRepository extends JpaRepository<SocialFeed, Integer> {

    @Query("SELECT new com.example.server.feed.dto.FeedInfo(f) FROM SocialFeed f")
    List<FeedInfo> findAllFeeds();

    @Query("SELECT f FROM SocialFeed f WHERE f.user.userId = :uploaderId ORDER BY f.uploadDatetime DESC")
    List<SocialFeed> findFeedsByUploaderId(@Param("uploaderId") int uploaderId);

    @Query("SELECT f FROM SocialFeed f JOIN FETCH f.user WHERE f.user.userId IN :followingIds AND f.feedId < :lastSeenId ORDER BY f.feedId DESC")
    List<SocialFeed> findFeedsByFollowingsAfter(@Param("followingIds") List<Integer> followerIds, @Param("lastSeenId") int lastSeenId, Pageable pageable);

    @Query("SELECT f FROM SocialFeed f JOIN FETCH f.user WHERE f.user.userId IN :followingIds ORDER BY f.feedId DESC")
    List<SocialFeed> findInitialFeedsByFollowings(@Param("followingIds") List<Integer> followerIds, Pageable pageable);

    @Query("SELECT f FROM SocialFeed f JOIN FETCH f.user")
    Page<SocialFeed> findAllWithUser(Pageable pageable);

    @Query("SELECT f FROM SocialFeed f JOIN FETCH f.user WHERE f.feedId IN :feedIds")
    List<SocialFeed> findWithIds(@Param("feedIds") List<Integer> feedIds);

    @Query("SELECT f.feedId FROM SocialFeed f")
    List<Integer> getAllIds();
}