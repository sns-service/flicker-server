package com.example.server.web.controller;

import com.example.server.auth.util.CookieUtils;
import com.example.server.web.entity.FeedDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;

import static com.example.server.auth.util.CookieUtils.generateAccessTokenCookie;
import static com.example.server.auth.util.CookieUtils.generateRefreshTokenCookie;

@Controller
@RequiredArgsConstructor
public class ApiController {

    @GetMapping
    public String indexPage(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = CookieUtils.extractAccessTokenFromCookies(request);
        String refreshToken = CookieUtils.extractRefreshTokenFromCookies(request);

        // 액세스 토큰은 있는데 리프레시 토큰이 없으면 → 비정상 상태, 쿠키 강제 삭제
        if (accessToken != null && refreshToken == null) {
            ResponseCookie expiredAccess = generateAccessTokenCookie("", 0);
            ResponseCookie expiredRefresh = generateRefreshTokenCookie("", 0);
            response.addHeader(HttpHeaders.SET_COOKIE, expiredAccess.toString());
            response.addHeader(HttpHeaders.SET_COOKIE, expiredRefresh.toString());
        }

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