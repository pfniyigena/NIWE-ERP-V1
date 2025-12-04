package com.niwe.erp.security.config;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade {
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public String getUsername() {
        Authentication auth = getAuthentication();
        return (auth != null && auth.isAuthenticated()) ? auth.getName() : null;
    }

    public org.springframework.security.core.userdetails.User getPrincipal() {
        Authentication auth = getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return null;
        return (org.springframework.security.core.userdetails.User) auth.getPrincipal();
    }
}
