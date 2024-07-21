package com.example.server.feed.repository;

import com.example.server.feed.entity.SocialFeed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface FeedPagingRepository extends PagingAndSortingRepository<SocialFeed, Integer> {
    Page<SocialFeed> findAll(Pageable pageable);
}
