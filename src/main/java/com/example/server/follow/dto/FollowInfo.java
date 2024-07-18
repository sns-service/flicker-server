package com.example.server.follow.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class FollowInfo {
    private int followId;
    private int userId;
    private int followerId;
    private ZonedDateTime followDatetime;

    @Builder
    public FollowInfo(int followId, int userId, int followerId, ZonedDateTime followDatetime) {
        this.followId = followId;
        this.userId = userId;
        this.followerId = followerId;
        this.followDatetime = followDatetime;
    }
}
