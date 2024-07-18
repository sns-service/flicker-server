package com.example.userserver.feed.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LikeResponse {
    int likeCount;
    boolean like;
}