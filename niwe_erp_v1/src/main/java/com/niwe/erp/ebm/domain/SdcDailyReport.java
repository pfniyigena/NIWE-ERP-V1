package com.niwe.erp.ebm.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.niwe.erp.common.domain.AbstractEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "EBM_SDC_DAILY_REPORT")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class SdcDailyReport extends AbstractEntity {
	/**
	 * The serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The normalInvoiceNumber
	 */
	@Column(name = "NORMAL_INVOICE_NUMBER", nullable = true)
	@Builder.Default
	private Integer normalInvoiceNumber = 0;
	@Column(name = "NORMAL_SALE_AMOUNT_INCLUDING_TAX_A", nullable = true)
	@Builder.Default
	private BigDecimal normalSaleAmountIncludingTaxA = BigDecimal.ZERO;

	@Column(name = "NORMAL_SALE_AMOUNT_INCLUDING_TAX_B1", nullable = true)
	@Builder.Default
	private BigDecimal normalSaleAmountIncludingTaxB1 = BigDecimal.ZERO;

	@Column(name = "NORMAL_SALE_AMOUNT_INCLUDING_TAX_B2", nullable = true)
	@Builder.Default
	private BigDecimal normalSaleAmountIncludingTaxB2 = BigDecimal.ZERO;

	@Column(name = "NORMAL_SALE_AMOUNT_INCLUDING_TAX_C", nullable = true)
	@Builder.Default
	private BigDecimal normalSaleAmountIncludingTaxC = BigDecimal.ZERO;

	@Column(name = "NORMAL_SALE_AMOUNT_INCLUDING_TAX_D", nullable = true)
	@Builder.Default
	private BigDecimal normalSaleAmountIncludingTaxD = BigDecimal.ZERO;
	@Column(name = "NORMAL_RETURN_AMOUNT_INCLUDING_TAX_A", nullable = true)
	@Builder.Default
	private BigDecimal normalReturnAmountIncludingTaxA = BigDecimal.ZERO;

	@Column(name = "NORMAL_RETURN_AMOUNT_INCLUDING_TAX_B1", nullable = true)
	@Builder.Default
	private BigDecimal normalReturnAmountIncludingTaxB1 = BigDecimal.ZERO;

	@Column(name = "NORMAL_RETURN_AMOUNT_INCLUDING_TAX_B2", nullable = true)
	@Builder.Default
	private BigDecimal normalReturnAmountIncludingTaxB2 = BigDecimal.ZERO;

	@Column(name = "NORMAL_RETURN_AMOUNT_INCLUDING_TAX_C", nullable = true)
	@Builder.Default
	private BigDecimal normalReturnAmountIncludingTaxC = BigDecimal.ZERO;

	@Column(name = "NORMAL_RETURN_AMOUNT_INCLUDING_TAX_D", nullable = true)
	@Builder.Default
	private BigDecimal normalReturnAmountIncludingTaxD = BigDecimal.ZERO;
	@Column(name = "TRAINING_INVOICE_NUMBER", nullable = true)
	@Builder.Default
	private Integer trainingInvoiceNumber = 0;
	@Column(name = "TRAINING_SALE_AMOUNT_INCLUDING_TAX_A", nullable = true)
	@Builder.Default
	private BigDecimal trainingSaleAmountIncludingTaxA = BigDecimal.ZERO;

	@Column(name = "TRAINING_SALE_AMOUNT_INCLUDING_TAX_B1", nullable = true)
	@Builder.Default
	private BigDecimal trainingSaleAmountIncludingTaxB1 = BigDecimal.ZERO;

	@Column(name = "TRAINING_SALE_AMOUNT_INCLUDING_TAX_B2", nullable = true)
	@Builder.Default
	private BigDecimal trainingSaleAmountIncludingTaxB2 = BigDecimal.ZERO;

	@Column(name = "TRAINING_SALE_AMOUNT_INCLUDING_TAX_C", nullable = true)
	@Builder.Default
	private BigDecimal trainingSaleAmountIncludingTaxC = BigDecimal.ZERO;

	@Column(name = "TRAINING_SALE_AMOUNT_INCLUDING_TAX_D", nullable = true)
	@Builder.Default
	private BigDecimal traingSaleAmountIncludingTaxD = BigDecimal.ZERO;
	@Column(name = "TRAINING_RETURN_AMOUNT_INCLUDING_TAX_A", nullable = true)
	@Builder.Default
	private BigDecimal trainingReturnAmountIncludingTaxA = BigDecimal.ZERO;

	@Column(name = "TRAINING_RETURN_AMOUNT_INCLUDING_TAX_B1", nullable = true)
	@Builder.Default
	private BigDecimal trainingReturnAmountIncludingTaxB1 = BigDecimal.ZERO;

	@Column(name = "TRAINING_RETURN_AMOUNT_INCLUDING_TAX_B2", nullable = true)
	@Builder.Default
	private BigDecimal trainingReturnAmountIncludingTaxB2 = BigDecimal.ZERO;

	@Column(name = "TRAINING_RETURN_AMOUNT_INCLUDING_TAX_C", nullable = true)
	@Builder.Default
	private BigDecimal trainingReturnAmountIncludingTaxC = BigDecimal.ZERO;

	@Column(name = "TRAINING_RETURN_AMOUNT_INCLUDING_TAX_D", nullable = true)
	@Builder.Default
	private BigDecimal trainingReturnAmountIncludingTaxD = BigDecimal.ZERO;
	@Column(name = "COPY_INVOICE_NUMBER", nullable = true)
	@Builder.Default
	private Integer copyInvoiceNumber = 0;
	@Column(name = "COPY_SALE_AMOUNT_INCLUDING_TAX_A", nullable = true)
	@Builder.Default
	private BigDecimal copySaleAmountIncludingTaxA = BigDecimal.ZERO;

	@Column(name = "COPY_SALE_AMOUNT_INCLUDING_TAX_B1", nullable = true)
	@Builder.Default
	private BigDecimal copySaleAmountIncludingTaxB1 = BigDecimal.ZERO;

	@Column(name = "COPY_SALE_AMOUNT_INCLUDING_TAX_B2", nullable = true)
	@Builder.Default
	private BigDecimal copySaleAmountIncludingTaxB2 = BigDecimal.ZERO;

	@Column(name = "COPY_SALE_AMOUNT_INCLUDING_TAX_C", nullable = true)
	@Builder.Default
	private BigDecimal copySaleAmountIncludingTaxC = BigDecimal.ZERO;

	@Column(name = "COPY_SALE_AMOUNT_INCLUDING_TAX_D", nullable = true)
	@Builder.Default
	private BigDecimal copySaleAmountIncludingTaxD = BigDecimal.ZERO;
	@Column(name = "COPY_RETURN_AMOUNT_INCLUDING_TAX_A", nullable = true)
	@Builder.Default
	private BigDecimal copyReturnAmountIncludingTaxA = BigDecimal.ZERO;

	@Column(name = "COPY_RETURN_AMOUNT_INCLUDING_TAX_B1", nullable = true)
	@Builder.Default
	private BigDecimal copyReturnAmountIncludingTaxB1 = BigDecimal.ZERO;

	@Column(name = "COPY_RETURN_AMOUNT_INCLUDING_TAX_B2", nullable = true)
	@Builder.Default
	private BigDecimal copyReturnAmountIncludingTaxB2 = BigDecimal.ZERO;

	@Column(name = "COPY_RETURN_AMOUNT_INCLUDING_TAX_C", nullable = true)
	@Builder.Default
	private BigDecimal copyReturnAmountIncludingTaxC = BigDecimal.ZERO;

	@Column(name = "COPY_RETURN_AMOUNT_INCLUDING_TAX_D", nullable = true)
	@Builder.Default
	private BigDecimal copyReturnAmountIncludingTaxD = BigDecimal.ZERO;
	@Column(name = "PROFORMA_INVOICE_NUMBER", nullable = true)
	@Builder.Default
	private Integer proformaInvoiceNumber = 0;

	@Column(name = "PROFORMA_SALE_AMOUNT_INCLUDING_TAX_A", nullable = true)
	@Builder.Default
	private BigDecimal proformaSaleAmountIncludingTaxA = BigDecimal.ZERO;

	@Column(name = "PROFORMA_SALE_AMOUNT_INCLUDING_TAX_B1", nullable = true)
	@Builder.Default
	private BigDecimal proformaSaleAmountIncludingTaxB1 = BigDecimal.ZERO;

	@Column(name = "PROFORMA_SALE_AMOUNT_INCLUDING_TAX_B2", nullable = true)
	@Builder.Default
	private BigDecimal proformaSaleAmountIncludingTaxB2 = BigDecimal.ZERO;

	@Column(name = "PROFORMA_SALE_AMOUNT_INCLUDING_TAX_C", nullable = true)
	@Builder.Default
	private BigDecimal proformaSaleAmountIncludingTaxC = BigDecimal.ZERO;

	@Column(name = "PROFORMA_SALE_AMOUNT_INCLUDING_TAX_D", nullable = true)
	@Builder.Default
	private BigDecimal proformaSaleAmountIncludingTaxD = BigDecimal.ZERO;

	@Column(name = "PROFORMA_RETURN_AMOUNT_INCLUDING_TAX_A", nullable = true)
	@Builder.Default
	private BigDecimal proformaReturnAmountIncludingTaxA = BigDecimal.ZERO;

	@Column(name = "PROFORMA_RETURN_AMOUNT_INCLUDING_TAX_B1", nullable = true)
	@Builder.Default
	private BigDecimal proformaReturnAmountIncludingTaxB1 = BigDecimal.ZERO;

	@Column(name = "PROFORMA_RETURN_AMOUNT_INCLUDING_TAX_B2", nullable = true)
	@Builder.Default
	private BigDecimal proformaReturnAmountIncludingTaxB2 = BigDecimal.ZERO;

	@Column(name = "PROFORMA_RETURN_AMOUNT_INCLUDING_TAX_C", nullable = true)
	@Builder.Default
	private BigDecimal proformaReturnAmountIncludingTaxC = BigDecimal.ZERO;

	@Column(name = "PROFORMA_RETURN_AMOUNT_INCLUDING_TAX_D", nullable = true)
	@Builder.Default
	private BigDecimal proformaReturnAmountIncludingTaxD = BigDecimal.ZERO;
	@Column(name = "TOTAL_INVOICE_NUMBER", nullable = true)
	@Builder.Default
	private Integer totalInvoiceNumber = 0;

	@Column(name = "TOTAL_NORMAL_INVOICE_NUMBER", nullable = true)
	@Builder.Default
	private Integer totalNormalInvoiceNumber = 0;

	@Column(name = "TOTAL_TRAINING_INVOICE_NUMBER", nullable = true)
	@Builder.Default
	private Integer totalTrainingInvoiceNumber = 0;

	@Column(name = "TOTAL_COPY_INVOICE_NUMBER", nullable = true)
	@Builder.Default
	private Integer totalCopyInvoiceNumber = 0;

	@Column(name = "TOTAL_PROFORMA_INVOICE_NUMBER", nullable = true)
	@Builder.Default
	private Integer totalProformaInvoiceNumber = 0;

	@Column(name = "TOTAL_SALE_TAX_AMOUNT", nullable = true)
	@Builder.Default
	private BigDecimal totalSaleTaxAmount = BigDecimal.ZERO;

	@Column(name = "TOTAL_SALE_AMOUNT_EXCLUDING_TAX", nullable = true)
	@Builder.Default
	private BigDecimal totalSaleAmountExcludingTax = BigDecimal.ZERO;

	@Column(name = "TOTAL_SALE_AMOUNT_INCLUDING_TAX", nullable = true)
	@Builder.Default
	private BigDecimal totalSaleAmountIncludingTax = BigDecimal.ZERO;

	@Column(name = "TOTAL_RETURN_TAX_AMOUNT", nullable = true)
	@Builder.Default
	private BigDecimal totalReturnTaxAmount = BigDecimal.ZERO;

	@Column(name = "TOTAL_RETURN_AMOUNT_EXCLUDING_TAX", nullable = true)
	@Builder.Default
	private BigDecimal totalReturnAmountExcludingTax = BigDecimal.ZERO;

	@Column(name = "TOTAL_RETURN_AMOUNT_INCLUDING_TAX", nullable = true)
	@Builder.Default
	private BigDecimal totalReturnAmountIncludingTax = BigDecimal.ZERO;

	@Column(name = "REPORT_DATE", nullable = false)
	@Builder.Default
	private LocalDate reportDate = LocalDate.now();
	/**
	 * The sdcInformation
	 */
	@ManyToOne
	@JoinColumn(name = "SDC_INFORMATION_ID", nullable = false)
	private SdcInformation sdcInformation;

}
