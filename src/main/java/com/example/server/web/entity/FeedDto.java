package com.example.server.web.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Getter
@NoArgsConstructor
public class FeedDto {

    private int feedId;
    private String imageId;
    private String uploaderName;
    private int uploaderId;

    private ZonedDateTime uploadDatetime;
    private String contents;
    private Long likes;

    private String formattedDate; // 추가된 필드

    public FeedDto(int feedId, String imageId, String uploaderName, int uploaderId, ZonedDateTime uploadDatetime, String contents, Long likes, String formattedDate) {
        this.feedId = feedId;
        this.imageId = imageId;
        this.uploaderName = uploaderName;
        this.uploaderId = uploaderId;
        this.uploadDatetime = uploadDatetime;
        this.contents = contents;
        this.likes = likes;
        this.formattedDate = formattedDate;
    }

    public void setFormattedDate(String formattedDate) {
        this.formattedDate = formattedDate;
    }
}