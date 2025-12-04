package com.niwe.erp.reconciliation.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.niwe.erp.common.domain.AbstractEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "RECONCILIATION_SALE_GENERATED")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class SaleGenerated extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Column(name = "BUYER_TIN")
	private String buyerTin;
	@Column(name = "BUYER_NAME")
	private String buyerName;
	@Column(name = "NATURE_OF_GOOD")
	private String natureOfGood;
	@Column(name = "RECEIPT_NUMBER",unique = true)
	private String receiptNumber;
	@Column(name = "INVOICE_DATE")
	private LocalDate invoiceDate;
	@Column(name = "TOTAL_AMOUNT_TAX_EXCLUSIVE")
	@Builder.Default
	private BigDecimal totalAmountTaxExclusive=BigDecimal.ZERO;
	@Column(name = "TOTAL_AMOUNT_EXAMPTED")
	@Builder.Default
	private BigDecimal totalAmountExampted=BigDecimal.ZERO;
	@Column(name = "TOTAL_AMOUNT_ZERO_RELATED")
	@Builder.Default
	private BigDecimal totalAmountZeroRelated=BigDecimal.ZERO;
	@Column(name = "TOTAL_AMOUNT_EXPORT")
	@Builder.Default
	private BigDecimal totalAmountExport=BigDecimal.ZERO;
	@Column(name = "TOTAL_AMOUNT_TAXABLE")
	@Builder.Default
	private BigDecimal totalAmountTaxable=BigDecimal.ZERO;
	@Column(name = "TOTAL_AMOUNT_VAT")
	@Builder.Default
	private BigDecimal totalAmountVat=BigDecimal.ZERO;
	

}
