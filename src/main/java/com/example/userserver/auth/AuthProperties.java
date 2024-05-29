package com.example.userserver.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AuthProperties {

    private static String accessSecret;
    private static String refreshSecret;

    @Value("${sns.jwt.access-key}")
    private void setAccessSecret(String accessSecret) {
        this.accessSecret = accessSecret;
    }

    @Value("${sns.jwt.secret-key}")
    private void setRefreshSecret(String refreshSecret) {
        this.refreshSecret = refreshSecret;
    }

    public static String getAccessSecret() {
        return accessSecret;
    }

    public static String getRefreshSecret() {
        return refreshSecret;
    }
}
