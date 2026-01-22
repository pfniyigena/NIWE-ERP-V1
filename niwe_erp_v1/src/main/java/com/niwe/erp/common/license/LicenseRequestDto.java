package com.niwe.erp.common.license;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LicenseRequestDto(@JsonProperty("code") String code, @JsonProperty("tinNumber") String tinNumber) {
}
