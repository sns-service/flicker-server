package com.example.userserver.auth.filter;

import com.example.userserver.auth.entity.CustomUserDetails;
import com.example.userserver.auth.repository.RefreshTokenRepository;
import com.example.userserver.auth.util.ResponseUtils;
import com.example.userserver.exception.DataNotFoundException;
import com.example.userserver.user.entity.User;
import com.example.userserver.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.example.userserver.auth.util.CookieUtils.*;
import static com.example.userserver.auth.util.JwtUtils.*;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private UserRepository userRepository;
    private RefreshTokenRepository refreshTokenRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository, RefreshTokenRepository refreshTokenRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    private final List<RequestMatcher> excludedUrlPatterns = Arrays.asList( // 이 필터 적용 안할 url 지정
            new AntPathRequestMatcher("/healthcheck/**")
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (isExcludedUrl(request)) {
            filterChain.doFilter(request, response); //이 필터 스킵, 다음꺼 실행.
            return;
        }
        String accessToken = extractAccessTokenFromCookies(request);

        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }
        Claims claims = null;

        int userId = 0;
        try {
            claims = getClaimsFromAccessToken(accessToken);
        } catch (ExpiredJwtException e) {
            // 엑세스 토큰이 만료되었을 때 리프레시 토큰이 유효하다면, 엑세스 토큰을 새로 발급해줍니다.
            String refreshToken = extractRefreshTokenFromCookies(request);

            if (!checkIfRefreshTokenValid(refreshToken)) {
                refreshTokenRepository.deleteByUserId(userId);
                handleExceptionResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "refresh-token expired");
                return;
            }
            claims = e.getClaims();

            int refreshTokenId = ((Integer) claims.get("refreshTokenId")).intValue();
            userId = ((Integer) claims.get("userId")).intValue();
            User user = userRepository.findById(userId).orElseThrow(() -> new DataNotFoundException());

            String newAccessToken = createAccessToken(user, refreshTokenId);
            ResponseCookie accessTokenCookie = generateAccessTokenCookie(newAccessToken);
            response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        } catch (MalformedJwtException e) {
            refreshTokenRepository.deleteByUserId(userId);
            filterChain.doFilter(request, response);
            return;
        }

        User user = findUserFromAccessTokenClaims(response, claims);
        this.saveAuthenticationToSecurityContextHolder(user);

        filterChain.doFilter(request, response);
    }

    private User findUserFromAccessTokenClaims(HttpServletResponse response, Claims claims) throws IOException {
        Optional<User> savedUser = userRepository.findByUsername(claims.get("username").toString());

        if (!savedUser.isPresent()) {
            ResponseUtils.createResponseBody(response, HttpStatus.UNAUTHORIZED);
            return null;
        }
        return savedUser.get();
    }

    private void saveAuthenticationToSecurityContextHolder(User user) {
        CustomUserDetails userDetails = new CustomUserDetails(user);

        // 인가 처리가 정상적으로 완료된다면 Authentication 객체 생성
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public boolean checkIfRefreshTokenValid(String refreshToken) {
        Claims claims = null;

        try {
            claims = getClaimsFromRefreshToken(refreshToken);
        } catch (ExpiredJwtException e) { // 리프레시 토큰 만료
            return false;
        } catch (MalformedJwtException e) { // 위변조 검사
            return false;
        }
        return true;
    }

    private boolean isExcludedUrl(HttpServletRequest request) {
        return excludedUrlPatterns.stream().anyMatch(pattern -> pattern.matches(request));
    }

    private void handleExceptionResponse(HttpServletResponse response, int status, String errorMessage) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(status);

        String json = String.format("{\"error\":\"%s\"}", errorMessage);
        response.getWriter().write(json);

        response.getWriter().flush();
        response.getWriter().close();
    }
}
