package com.niwe.erp.common.license;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class LicenseAspect {

    private final LicenseService licenseService;

    @Before("@annotation(RequiresActiveLicense)")
    public void checkLicense() {

        LicenseStatus status = licenseService.getStatus();
        log.info("=====LicenseStatus:{}",status);

        if (status != LicenseStatus.ACTIVE) {
            throw new LicenseLockedException(
                    "Read-only mode enabled."
            );
        }
    }
}

