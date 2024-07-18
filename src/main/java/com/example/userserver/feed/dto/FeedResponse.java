package com.example.userserver.feed.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
@AllArgsConstructor
@Builder
public class FeedResponse {

    private int feedId;
    private String imageId;
    private int uploaderId;
    private ZonedDateTime uploadDatetime;
    private String contents;
}
