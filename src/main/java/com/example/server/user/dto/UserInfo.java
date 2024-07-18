package com.example.server.user.dto;

import com.example.server.user.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfo {
    private int userId;
    private String username;
    private String email;

    public UserInfo(User user) {
        this(user.getUserId(), user.getUsername(), user.getEmail());
    }

    public UserInfo(int userId, String username, String email) {
        this.userId = userId;
        this.username = username;
        this.email = email;
    }
}
