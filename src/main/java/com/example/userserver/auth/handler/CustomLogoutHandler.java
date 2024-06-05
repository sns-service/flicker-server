package com.example.userserver.auth.handler;

import com.example.userserver.auth.repository.RefreshTokenRepository;
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

import static com.example.userserver.auth.util.CookieUtils.*;
import static com.example.userserver.auth.util.JwtUtils.getClaimsFromAccessToken;
import static com.example.userserver.auth.util.ResponseUtils.createResponseBody;

public class CustomLogoutHandler implements LogoutSuccessHandler {

    private RefreshTokenRepository refreshTokenRepository;

    public CustomLogoutHandler(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String accessToken = extractAccessTokenFromCookies(request);

        Claims claims = getClaimsFromAccessToken(accessToken);
        Long refreshTokenId = getRefreshTokenIdFromClaims(claims);

        refreshTokenRepository.findById(refreshTokenId).ifPresent(refreshToken -> refreshTokenRepository.delete(refreshToken));
        ResponseCookie refreshTokenCookie = generateRefreshTokenCookie(null, 0);
        ResponseCookie accessTokenCookie = generateAccessTokenCookie(null, 0);

        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());

        createResponseBody(response, HttpStatus.OK);
    }

    private Long getRefreshTokenIdFromClaims(Claims claims) {
        return ((Integer)claims.get("refreshTokenId")).longValue();
    }
}