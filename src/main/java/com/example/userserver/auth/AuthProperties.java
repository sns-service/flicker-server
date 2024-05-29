package com.example.userserver.auth;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class AuthProperties {

    private String accessSecret;
    private String refreshSecret;

    @Value("${sns.jwt.access-key}")
    private void setAccessSecret(String accessSecret) {
        this.accessSecret = accessSecret;
    }

    @Value("${sns.jwt.secret-key}")
    private void setRefreshSecret(String refreshSecret) {
        this.refreshSecret = refreshSecret;
    }
}
