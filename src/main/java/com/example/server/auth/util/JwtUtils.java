package com.example.server.auth.util;

import com.example.server.auth.AuthProperties;
import com.example.server.user.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
public class JwtUtils {

    private static final long TOKEN_VALIDITY_TIME_IN_SECONDS = 1000;
    private static final long TOKEN_VALIDITY_TIME_IN_MINUTES = TOKEN_VALIDITY_TIME_IN_SECONDS * 60;
    private static final long TOKEN_VALIDITY_TIME_IN_HOURS = TOKEN_VALIDITY_TIME_IN_MINUTES * 60;

    @Getter
    private static final long ACCESS_TOKEN_VALIDITY_TIME = TOKEN_VALIDITY_TIME_IN_MINUTES * 15; // 15분

    @Getter
    private static final long REFRESH_TOKEN_VALIDITY_TIME = TOKEN_VALIDITY_TIME_IN_MINUTES * 60 * 48;  // 48시간

    public static String createAccessToken(User user, int refreshTokenId) {
        return Jwts.builder()
                .setSubject("accessToken")
                .setClaims(createAccessTokenClaims(user, refreshTokenId))
                .setExpiration(createTokenExpiration(ACCESS_TOKEN_VALIDITY_TIME))
                .signWith(createSigningKey(AuthProperties.getAccessSecret()), SignatureAlgorithm.HS256)
                .compact();
    }

    public static String createRefreshToken(User user) {
        return Jwts.builder()
                .setSubject("refreshToken")
                .setClaims(createRefreshTokenClaims(user))
                .setExpiration(createTokenExpiration(REFRESH_TOKEN_VALIDITY_TIME))
                .signWith(createSigningKey(AuthProperties.getRefreshSecret()), SignatureAlgorithm.HS256)
                .compact();
    }

    private static Date createTokenExpiration(long expirationTime) {
        Date expiration = new Date(System.currentTimeMillis() + expirationTime);
        return expiration;
    }

    private static Key createSigningKey(String tokenSecret) {
        byte[] keyBytes = Decoders.BASE64.decode(tokenSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private static Map<String, Object> createAccessTokenClaims(User user, int refreshTokenId) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", user.getUserId());
        map.put("username", user.getUsername());
        map.put("email", user.getEmail());
        map.put("refreshTokenId", refreshTokenId);
        return map;
    }

    private static Map<String, Object> createRefreshTokenClaims(User user) {
        Map<String, Object> map = new HashMap<>();
        map.put("email", user.getEmail());
        map.put("username", user.getUsername());
        return map;
    }

    public static Claims getClaimsFromAccessToken(String accessToken) {
        return Jwts.parserBuilder().setSigningKey(AuthProperties.getAccessSecret()).build().parseClaimsJws(accessToken).getBody();
    }

    public static Claims getClaimsFromRefreshToken(String refreshToken) {
        return Jwts.parserBuilder().setSigningKey(AuthProperties.getRefreshSecret()).build().parseClaimsJws(refreshToken).getBody();
    }
}
