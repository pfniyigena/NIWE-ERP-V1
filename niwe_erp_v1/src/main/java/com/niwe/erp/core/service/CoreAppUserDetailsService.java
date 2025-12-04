package com.niwe.erp.core.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.niwe.erp.core.domain.CorePermission;
import com.niwe.erp.core.domain.CoreRole;
import com.niwe.erp.core.domain.CoreUser;
import com.niwe.erp.core.repository.CoreUserRepository;

@Service
public class CoreAppUserDetailsService implements UserDetailsService {
    private final CoreUserRepository coreUserRepository;

    public CoreAppUserDetailsService(CoreUserRepository coreUserRepository) { this.coreUserRepository = coreUserRepository; }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CoreUser u = coreUserRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Set<GrantedAuthority> auths = new HashSet<>();
        CoreRole r =u.getRole();
        auths.add(new SimpleGrantedAuthority(r.getName()));
        for (CorePermission p : r.getPermissions()) {
            auths.add(new SimpleGrantedAuthority(p.getName())); // treat permission names as authorities
        }
       
        return new org.springframework.security.core.userdetails.User(u.getUsername(), u.getPassword(), u.isEnabled(),
                true, true, true, auths);
    }
}
