package com.niwe.erp.web.api.dto;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SaleRequest(@JsonProperty("niwe_header") NiweHeaderRequest niweHeaderRequest,
		@JsonProperty("customer_name") String customerName, @JsonProperty("customer_tin") String customerTin,
		@JsonProperty("customer_phone") String customerPhone,
		@JsonProperty("external_reference") String externalReference,
		@JsonProperty("transaction_type") String transactionType, @JsonProperty("payment_method") String paymentMethod,
		@JsonProperty("confirmed_by") String confirmedBy, @JsonProperty("sale_date") String saleDate,
		@JsonProperty("total_gross_amount") BigDecimal totalGrossAmount,
		@JsonProperty("total_discount_amount") BigDecimal totalDiscountAmount,
		@JsonProperty("total_tax_amount") BigDecimal totalTaxAmount,
		@JsonProperty("total_amount") BigDecimal totalAmount, List<SaleItemRequest> items) {

 

}
