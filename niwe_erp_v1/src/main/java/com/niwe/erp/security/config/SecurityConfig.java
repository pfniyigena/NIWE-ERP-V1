package com.niwe.erp.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.niwe.erp.core.service.CoreAppUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	private final CoreAppUserDetailsService userDetailsService;
	private final CustomAccessDeniedHandler accessDeniedHandler;

	public SecurityConfig(CoreAppUserDetailsService uds, CustomAccessDeniedHandler accessDeniedHandler) {
		this.userDetailsService = uds;
		this.accessDeniedHandler = accessDeniedHandler;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(auth -> auth
				.requestMatchers("assets/**", "/api/**", "/login", "/error", "/swagger-ui.html", "/swagger-ui/**",
						"/v3/api-docs/**")
				.permitAll().requestMatchers("/admin/**").hasRole("ADMIN").anyRequest().authenticated())
				.formLogin(form -> form.loginPage("/login").permitAll().defaultSuccessUrl("/", true))
				.logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/login?logout"))
				.exceptionHandling(ex -> ex.accessDeniedHandler(accessDeniedHandler)).csrf(csrf -> csrf.disable());

		return http.build();
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(11);
	}
}
