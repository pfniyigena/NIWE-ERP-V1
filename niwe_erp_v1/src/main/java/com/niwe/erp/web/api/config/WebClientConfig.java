package com.niwe.erp.web.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

	@Bean
	public WebClient webClient() {
	    return WebClient.builder()
	            .filter(WebClientLoggingFilter.logRequest())
	            .filter(WebClientLoggingFilter.logResponse())
	            .filter(WebClientLoggingFilter.logResponseBody())
	            .build();
	}

}
