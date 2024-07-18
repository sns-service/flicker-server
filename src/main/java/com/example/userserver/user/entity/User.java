package com.example.userserver.user.entity;

import com.example.userserver.feed.entity.SocialFeed;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<SocialFeed> feeds = new ArrayList<>();

    private String username;
    private String email;
    private String password;

    @Builder
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}