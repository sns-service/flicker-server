package com.example.server.web.controller;

import com.example.server.web.entity.FeedDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;

@Controller
@RequiredArgsConstructor
public class ApiController {

    @GetMapping
    public String indexPage() {
        System.out.println("indexPage");
        return "index";
    }

    @GetMapping("/mypage")
    public String myPage() {
        return "mypage";
    }

    @GetMapping("/signup")
    public String signUp() {
        return "signup";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("createpost")
    public String createPost() {
        return "createpost";
    }

    @GetMapping("/userposts")
    public String getUserPosts() {
        return "userposts";
    }

    private FeedDto transformPost(LinkedHashMap post) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy. M. d. a hh:mm:ss");
        ZonedDateTime uploadDatetime = ZonedDateTime.parse((String) post.get("uploadDatetime"));
        String formattedDate = uploadDatetime.format(formatter);

        return new FeedDto(
                (int) post.get("feedId"),
                (String) post.get("imageId"),
                (String) post.get("uploaderName"),
                (int) post.get("uploaderId"),
                uploadDatetime,
                (String) post.get("contents"),
                ((Number) post.get("likes")).longValue(),
                formattedDate
        );
    }
}