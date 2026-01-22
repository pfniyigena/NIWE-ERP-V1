package com.niwe.erp.common.license;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class LicenseApiClient {

	private final WebClient webClient;

	public LicenseStatus checkLicense(String code, String tinNumber) {

		LicenseRequestDto request = new LicenseRequestDto(code, tinNumber);

		return webClient.post().uri("http://54.36.163.1:8040/ebm2-security/rest/api/post/license").bodyValue(request)
				.exchangeToMono(res -> {
					String licenseStatus = res.headers().header("ERROR_KEY").stream().findFirst().orElse(null);

					log.info("checkLicense:{}", licenseStatus);
					if (licenseStatus != null) {
						return Mono.just(LicenseStatus.LOCKED);
					}

					if (res.statusCode().isError()) {
						return res.bodyToMono(String.class)
								.flatMap(body -> Mono.error(new RuntimeException("API error: " + body)));
					}

					return Mono.just(LicenseStatus.ACTIVE);
				}).block();
	}

}
