package com.example.server.feed.repository;

import com.example.server.feed.dto.FeedInfo;
import com.example.server.feed.dto.FeedResponse;
import com.example.server.feed.entity.SocialFeed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FeedJpaRepository extends JpaRepository<SocialFeed, Integer> {

    @Query("SELECT new com.example.server.feed.dto.FeedInfo(f) FROM SocialFeed f")
    List<FeedInfo> findAllFeeds();

    @Query("SELECT new com.example.server.feed.dto.FeedResponse(f.feedId, f.imageId, f.user.userId, f.uploadDatetime, f.contents) FROM SocialFeed f WHERE f.user.userId = :uploaderId")
    List<FeedResponse> findFeedsInfoByUploaderId(@Param("uploaderId") int uploaderId);
}