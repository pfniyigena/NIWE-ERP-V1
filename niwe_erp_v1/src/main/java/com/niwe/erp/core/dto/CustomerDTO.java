package com.niwe.erp.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CustomerDTO(@JsonProperty("customer_name") String customerName,
		@JsonProperty("internal_code") String internalCode,
		@JsonProperty("tin_number") String tinNumber,
		@JsonProperty("customer_phone") String customerPhone, @JsonProperty("customer_email") String customerEmail) {
}
