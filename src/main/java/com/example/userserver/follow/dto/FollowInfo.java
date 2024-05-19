package com.example.userserver.follow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class FollowInfo {
    private int followId;
    private int userId;
    private int followerId;
    private ZonedDateTime followDatetime;
}
