package com.example.userserver.feed.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateFeedRequest {

    private String imageId;
    private int uploaderId;
    private String contents;
}
