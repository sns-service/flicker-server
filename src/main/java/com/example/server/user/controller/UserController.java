package com.example.server.user.controller;

import com.example.server.user.dto.UserInfo;
import com.example.server.user.dto.UserRequest;
import com.example.server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserInfo signUpUser(@RequestBody UserRequest userRequest) {
        return userService.createUser(userRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserInfo> getUserInfo(@PathVariable("id") int id) {
        UserInfo user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<UserInfo> getUserInfoByName(@PathVariable("name") String name) {
        UserInfo user = userService.getUserByName(name);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/signIn")
    public UserInfo signIn(@RequestBody UserRequest signInRequest) {
        return userService.signIn(signInRequest);
    }
}
