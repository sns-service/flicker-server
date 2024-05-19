package com.example.userserver.user.dto;

import lombok.Getter;

@Getter
public class UserRequest {
    private String username;
    private String email;
    private String plainPassword;
}
