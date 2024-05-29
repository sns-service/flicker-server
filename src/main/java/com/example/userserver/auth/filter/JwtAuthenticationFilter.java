package com.example.userserver.auth.filter;

import com.example.userserver.auth.entity.CustomUserDetails;
import com.example.userserver.auth.entity.RefreshToken;
import com.example.userserver.auth.repository.RefreshTokenRepository;
import com.example.userserver.auth.util.ResponseUtils;
import com.example.userserver.user.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

import static com.example.userserver.auth.util.CookieUtils.generateAccessTokenCookie;
import static com.example.userserver.auth.util.CookieUtils.generateRefreshTokenCookie;
import static com.example.userserver.auth.util.JwtUtils.createAccessToken;
import static com.example.userserver.auth.util.JwtUtils.createRefreshToken;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private RefreshTokenRepository refreshTokenRepository;

    public JwtAuthenticationFilter(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        setFilterProcessesUrl("/api/users/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            User user = new ObjectMapper().readValue(request.getReader(), User.class);

            UsernamePasswordAuthenticationToken userToken =
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

            this.setDetails(request, userToken);

            // AuthenticationManager 에 인증을 위임한다.
            return getAuthenticationManager().authenticate(userToken);
        } catch (IOException e) {
            throw new AuthenticationServiceException("아이디와 비밀번호를 올바르게 입력해주세요.");
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        // 1. 로그인 성공된 유저 조회
        User user = ((CustomUserDetails) authResult.getPrincipal()).getUser();

        // 2. Refresh Token DB 저장 (해당 유저의 리프레시 토큰이 이미 존재한다면, 삭제 후 저장), 쿠키에 담아 반환
        String refreshToken = createRefreshToken(user);

        ResponseCookie refreshTokenCookie = generateRefreshTokenCookie(refreshToken);
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

        int refreshTokenId = saveRefreshToken(user, refreshToken);

        // 3. AccessToken 발급, 쿠키에 담아 보냄
        String accessToken = createAccessToken(user, refreshTokenId);

        ResponseCookie accessTokenCookie = generateAccessTokenCookie(accessToken);
        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());

        ResponseUtils.createResponseBody(response, HttpStatus.OK);
    }

    private int saveRefreshToken(User user, String refreshToken) {
        try{
            refreshTokenRepository.findByUserId(user.getUserId())
                    .ifPresent(refreshTokenRepository::delete);

            RefreshToken newRefreshToken = new RefreshToken(refreshToken, user);

            return refreshTokenRepository.save(newRefreshToken).getId();
        } catch (NullPointerException e) {
            throw new AuthenticationServiceException("유효하지 않은 사용자입니다.");
        }
    }
}
