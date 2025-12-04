package com.niwe.erp.invoicing.web.form;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class InvoiceLineForm {
	private String id;
	private String itemName;
	private String itemCode;
	private String taxCode;
	@Builder.Default
	private BigDecimal taxType = BigDecimal.ZERO;
	@Builder.Default
	private BigDecimal quantity = BigDecimal.ZERO;
	@Builder.Default
	private BigDecimal unitPrice = BigDecimal.ZERO;
	@Builder.Default
	private BigDecimal lineTotal = BigDecimal.ZERO;
	@Builder.Default
	private BigDecimal taxAmount = BigDecimal.ZERO;
	@Builder.Default
	private BigDecimal grossAmount = BigDecimal.ZERO;
	@Builder.Default
	private BigDecimal amountToPay = BigDecimal.ZERO;

	

	public BigDecimal getLineTotal() {
		return unitPrice != null && quantity != null ? unitPrice.multiply(quantity) : BigDecimal.ZERO;
	}

}
