package com.example.server.follow.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Entity
@Table
@Getter
@NoArgsConstructor
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int followId;

    private int userId;

    private int followerId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "follow_datetime")
    private ZonedDateTime followDatetime;

    public Follow(int userId, int followerId) {
        this.userId = userId;
        this.followerId = followerId;
    }

    @PrePersist
    protected void onCreate() {
        followDatetime = ZonedDateTime.now();
    }
}
