package com.example.userserver.follow.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FollowRequest {

    private int userId;
    private int followerId;
}
