package com.example.server.auth;

import com.example.server.auth.util.CookieUtils;
import com.example.server.exception.DataNotFoundException;
import com.example.server.user.entity.User;
import com.example.server.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.server.auth.util.CookieUtils.generateAccessTokenCookie;
import static com.example.server.auth.util.JwtUtils.*;

@RestController
@RequestMapping("/api/users/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<UserAuthInfo> checkAuth(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = CookieUtils.extractRefreshTokenFromCookies(request);
        String accessToken = CookieUtils.extractAccessTokenFromCookies(request);

        if(refreshToken == null || accessToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new UserAuthInfo(-1));
        }

        try {
            Claims claims = getClaimsFromAccessToken(accessToken);
            return ResponseEntity.ok().body(new UserAuthInfo(extractUserId(claims)));
        } catch (ExpiredJwtException e) {
            return handleExpiredAccessToken(e, refreshToken, response);
        } catch (MalformedJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new UserAuthInfo(-1));
        }
    }

    private ResponseEntity<UserAuthInfo> handleExpiredAccessToken(ExpiredJwtException e, String refreshToken, HttpServletResponse response) {
        if (!isRefreshTokenValid(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new UserAuthInfo(-1));
        }

        Claims claims = e.getClaims();
        int userId = extractUserId(claims);
        User user = userRepository.findById(userId).orElseThrow(DataNotFoundException::new);

        int refreshTokenId = extractRefreshTokenId(claims);
        String newAccessToken = createAccessToken(user, refreshTokenId);

        ResponseCookie accessTokenCookie = generateAccessTokenCookie(newAccessToken);
        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());

        return ResponseEntity.ok().body(new UserAuthInfo(userId));
    }

    private boolean isRefreshTokenValid(String refreshToken) {
        try {
            getClaimsFromRefreshToken(refreshToken);
            return true;
        } catch (ExpiredJwtException | MalformedJwtException e) {
            return false;
        }
    }

    private int extractUserId(Claims claims) {
        return ((Integer) claims.get("userId")).intValue();
    }

    private int extractRefreshTokenId(Claims claims) {
        return ((Integer) claims.get("refreshTokenId")).intValue();
    }

    @Getter
    class UserAuthInfo {
        private final int userId;

        public UserAuthInfo(int userId) {
            this.userId = userId;
        }
    }
}
