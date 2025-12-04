package com.niwe.erp.security.config;
import com.niwe.erp.core.service.CoreMenuService;
import com.niwe.erp.core.view.CoreMenuDto;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class MenuAdvice {

    private final CoreMenuService coreMenuService;

    public MenuAdvice(CoreMenuService coreMenuService) {
        this.coreMenuService = coreMenuService;
    }

    @ModelAttribute("menuItems")
    public List<CoreMenuDto> menuItems(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return List.of();
        }
        Set<String> authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        return coreMenuService.getVisibleMenus(authorities);
    }
}

