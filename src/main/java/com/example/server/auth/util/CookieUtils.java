package com.example.server.auth.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;

public class CookieUtils {

    public static ResponseCookie generateRefreshTokenCookie(String refreshToken) {
        return ResponseCookie.from("sns-refresh", refreshToken)
                .httpOnly(true)
                .sameSite("None")
                .secure(true)
                .path("/")
                .maxAge(JwtUtils.getREFRESH_TOKEN_VALIDITY_TIME())
                .build();
    }

    public static ResponseCookie generateRefreshTokenCookie(String refreshToken, long time) {
        return ResponseCookie.from("sns-refresh", refreshToken)
                .httpOnly(true)
                .sameSite("None")
                .secure(true)
                .path("/")
                .maxAge(time)
                .build();
    }

    public static ResponseCookie generateAccessTokenCookie(String accessToken, long time) {
        return ResponseCookie.from("sns-access", accessToken)
                .httpOnly(true)
                .sameSite("None")
                .secure(true)
                .path("/")
                .maxAge(time)
                .build();
    }

    public static ResponseCookie generateAccessTokenCookie(String accessToken) {
        return ResponseCookie.from("sns-access", accessToken)
                .httpOnly(true)
                .sameSite("None")
                .secure(true)
                .path("/")
                .maxAge(JwtUtils.getACCESS_TOKEN_VALIDITY_TIME())
                .build();
    }

    public static String extractAccessTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("sns-access".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public static String extractRefreshTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("sns-refresh".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
