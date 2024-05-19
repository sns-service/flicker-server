package com.example.userserver.follow.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Entity
@Table
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int followId;

    private int userId;

    private int followerId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "follow_datetime")
    private ZonedDateTime followDatetime;

    @PrePersist
    protected void onCreate() {
        followDatetime = ZonedDateTime.now();
    }
}
