package com.niwe.erp.common.license;

import java.time.Duration;
import java.time.Instant;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LicenseService {

	private volatile LicenseStatus cachedStatus = LicenseStatus.ACTIVE;
	private volatile Instant lastChecked = Instant.EPOCH;
	private static final Duration CACHE_DURATION = Duration.ofMinutes(10);
	private final LicenseApiClient licenseApiClient;

	public LicenseStatus getStatus() {

		// ✅ Use cache if still valid
		if (Instant.now().minus(CACHE_DURATION).isBefore(lastChecked)) {
			return cachedStatus;
		}

		synchronized (this) {
			// Double-check locking
			if (Instant.now().minus(CACHE_DURATION).isBefore(lastChecked)) {
				return cachedStatus;
			}

			try {
				cachedStatus = fetchFromApi();
				lastChecked = Instant.now();
			} catch (LicenseLockedException e) {
				cachedStatus = LicenseStatus.LOCKED;
				lastChecked = Instant.now();
			} catch (Exception e) {
				// Fail-safe: keep last known status
			}

			return cachedStatus;
		}
	}

	private LicenseStatus fetchFromApi() {
		return licenseApiClient.checkLicense("COMP-0121", "119789715");
	}
	@Scheduled(fixedDelay = 10 * 60 * 1000)
	public void refreshLicense() {
		getStatus() ;
	}
}
