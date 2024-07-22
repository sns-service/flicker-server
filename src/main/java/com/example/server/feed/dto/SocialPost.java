package com.example.server.feed.dto;

import com.example.server.feed.entity.SocialFeed;
import com.example.server.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SocialPost {

    private int feedId;
    private String imageId;
    private String uploaderName;
    private int uploaderId;

    private ZonedDateTime uploadDatetime;
    private String contents;
    private int likes;

    public SocialPost(FeedInfo post, int likes) {
        this(post.getFeedId(), post.getImageId(), post.getUploaderName(), post.getUploaderId(), post.getUploadDatetime(), post.getContents(), likes);
    }

    public SocialPost(SocialFeed feed, int likes) {
        User user = feed.getUser();
        this.feedId = feed.getFeedId();
        this.imageId = feed.getImageId();
        this.uploaderName = user.getUsername();
        this.uploaderId = user.getUserId();
        this.uploadDatetime = feed.getUploadDatetime();
        this.contents = feed.getContents();
        this.likes = likes;
    }
}