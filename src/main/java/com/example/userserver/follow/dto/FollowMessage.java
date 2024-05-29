package com.example.userserver.follow.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FollowMessage {

    private int userId;
    private int followerId;
    private boolean follow;
}
