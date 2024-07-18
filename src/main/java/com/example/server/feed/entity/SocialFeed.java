package com.example.server.feed.entity;

import com.example.server.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class SocialFeed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int feedId;

    private String imageId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "upload_datetime")
    private ZonedDateTime uploadDatetime;

    private String contents;

    @PrePersist
    protected void onCreate() {
        uploadDatetime = ZonedDateTime.now();
    }
}
