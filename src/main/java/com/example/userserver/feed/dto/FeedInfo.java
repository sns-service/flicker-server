package com.example.userserver.feed.dto;

import com.example.userserver.feed.entity.SocialFeed;
import com.example.userserver.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FeedInfo {

    private int feedId;
    private String imageId;
    private int uploaderId;
    private String uploaderName;

    @Setter
    private ZonedDateTime uploadDatetime;
    private String contents;

    public FeedInfo(SocialFeed feed) {
        User user = feed.getUser();
        this.feedId = feed.getFeedId();
        this.imageId = feed.getImageId();
        this.uploaderId = user.getUserId();
        this.uploaderName = user.getUsername();
        this.uploadDatetime = feed.getUploadDatetime();
        this.contents = feed.getContents();
    }
}
