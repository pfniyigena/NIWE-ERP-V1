package com.niwe.erp.core.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CoreItemListDTO(@JsonProperty("internal_code") String internalCode,
		@JsonProperty("item_code") String itemCode, @JsonProperty("external_item_code") String externalItemCode, @JsonProperty("item_name") String itemName,
		@JsonProperty("barcode") String barcode, @JsonProperty("unit_price") BigDecimal unitPrice,
		@JsonProperty("unit_cost") BigDecimal unitCost, @JsonProperty("tax_code") String taxCode,
		@JsonProperty("tax_rate") BigDecimal taxRate, @JsonProperty("classification_code") String classificationCode,
		@JsonProperty("packaging_code") String packagingCode, @JsonProperty("country_code") String countryCode) {
}
