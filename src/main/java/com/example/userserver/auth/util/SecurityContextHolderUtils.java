package com.example.userserver.auth.util;

import com.example.userserver.auth.entity.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityContextHolderUtils {

    public static int getUserId() {
        CustomUserDetails principal = getCustomUserDetails();
        return principal.getUser().getUserId();
    }

    public static CustomUserDetails getCustomUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("authentication is not valid");
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof CustomUserDetails)) {
            throw new IllegalStateException();
        }

        return (CustomUserDetails) principal;
    }
}