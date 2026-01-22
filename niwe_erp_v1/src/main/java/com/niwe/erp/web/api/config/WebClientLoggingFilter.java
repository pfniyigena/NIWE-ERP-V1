package com.niwe.erp.web.api.config;

import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class WebClientLoggingFilter {

	public static ExchangeFilterFunction logRequest() {
		return ExchangeFilterFunction.ofRequestProcessor(request -> {
			log.info("➡️ REQUEST: {} {}", request.method(), request.url());

			request.headers()
					.forEach((name, values) -> values.forEach(value -> log.info("➡️ HEADER: {}={}", name, value)));

			return Mono.just(request);
		});
	}

	public static ExchangeFilterFunction logResponse() {
		return ExchangeFilterFunction.ofResponseProcessor(response -> {
			log.info("⬅️ RESPONSE STATUS: {}", response.statusCode());

			response.headers().asHttpHeaders()
					.forEach((name, values) -> values.forEach(value -> log.info("⬅️ HEADER: {}={}", name, value)));

			return Mono.just(response);
		});
	}

	public static ExchangeFilterFunction logResponseBody() {
		return ExchangeFilterFunction
				.ofResponseProcessor(response -> response.bodyToMono(String.class).defaultIfEmpty("").flatMap(body -> {
					log.info("⬅️ BODY:\n{}", body);

					// Rebuild response so downstream can read it
					return Mono.just(ClientResponse.create(response.statusCode())
							.headers(headers -> headers.addAll(response.headers().asHttpHeaders())).body(body).build());
				}));
	}

}
