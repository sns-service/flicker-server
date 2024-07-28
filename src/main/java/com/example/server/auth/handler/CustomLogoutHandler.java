package com.example.server.auth.handler;

import com.example.server.auth.repository.RefreshTokenRepository;
import com.example.server.auth.util.CookieUtils;
import com.example.server.auth.util.JwtUtils;
import com.example.server.auth.util.ResponseUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;

public class CustomLogoutHandler implements LogoutSuccessHandler {

    private RefreshTokenRepository refreshTokenRepository;

    public CustomLogoutHandler(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String accessToken = CookieUtils.extractAccessTokenFromCookies(request);

        Claims claims = JwtUtils.getClaimsFromAccessToken(accessToken);
        Long refreshTokenId = getRefreshTokenIdFromClaims(claims);

        refreshTokenRepository.findById(refreshTokenId).ifPresent(refreshToken -> refreshTokenRepository.delete(refreshToken));
        ResponseCookie refreshTokenCookie = CookieUtils.generateRefreshTokenCookie(null, 0);
        ResponseCookie accessTokenCookie = CookieUtils.generateAccessTokenCookie(null, 0);

        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());

        ResponseUtils.createResponseBody(response, HttpStatus.OK);
    }

    private Long getRefreshTokenIdFromClaims(Claims claims) {
        return ((Integer)claims.get("refreshTokenId")).longValue();
    }
}