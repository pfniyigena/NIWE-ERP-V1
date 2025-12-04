package com.niwe.erp.ebm.domain;

import java.math.BigDecimal;

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
@Table(name = "EBM_SDC_COUNTER")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class SdcCounter extends AbstractEntity {
	/**
	 * The serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The totalNormalInvoiceNumber
	 */
	@Column(name = "TOTAL_NORMAL_INVOICE_NUMBER", nullable = true, columnDefinition = "integer default 0")
	@Builder.Default
	private Integer totalNormalInvoiceNumber = 0;
	/**
	 * The normalSaleAmountIncludingTaxA
	 */
	@Column(name = "NORMAL_SALE_AMOUNT_INCLUDING_TAX_A", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal normalSaleAmountIncludingTaxA = BigDecimal.ZERO;

	/**
	 * The normalSaleAmountIncludingTaxB1
	 */
	@Column(name = "NORMAL_SALE_AMOUNT_INCLUDING_TAX_B1", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal normalSaleAmountIncludingTaxB1 = BigDecimal.ZERO;

	/**
	 * The normalSaleAmountIncludingTaxB2
	 */
	@Column(name = "NORMAL_SALE_AMOUNT_INCLUDING_TAX_B2", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal normalSaleAmountIncludingTaxB2 = BigDecimal.ZERO;

	/**
	 * The normalSaleAmountIncludingTaxC
	 */
	@Column(name = "NORMAL_SALE_AMOUNT_INCLUDING_TAX_C", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal normalSaleAmountIncludingTaxC = BigDecimal.ZERO;

	/**
	 * The normalSaleAmountIncludingTaxD
	 */
	@Column(name = "NORMAL_SALE_AMOUNT_INCLUDING_TAX_D", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal normalSaleAmountIncludingTaxD = BigDecimal.ZERO;
	/**
	 * The normalReturnAmountIncludingTaxA
	 */
	@Column(name = "NORMAL_RETURN_AMOUNT_INCLUDING_TAX_A", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal normalReturnAmountIncludingTaxA = BigDecimal.ZERO;

	/**
	 * The normalReturnAmountIncludingTaxB1
	 */
	@Column(name = "NORMAL_RETURN_AMOUNT_INCLUDING_TAX_B1", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal normalReturnAmountIncludingTaxB1 = BigDecimal.ZERO;

	/**
	 * The normalReturnAmountIncludingTaxB2
	 */
	@Column(name = "NORMAL_RETURN_AMOUNT_INCLUDING_TAX_B2", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal normalReturnAmountIncludingTaxB2 = BigDecimal.ZERO;

	/**
	 * The normalReturnAmountIncludingTaxC
	 */
	@Column(name = "NORMAL_RETURN_AMOUNT_INCLUDING_TAX_C", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal normalReturnAmountIncludingTaxC = BigDecimal.ZERO;

	/**
	 * The normalReturnAmountIncludingTaxD
	 */
	@Column(name = "NORMAL_RETURN_AMOUNT_INCLUDING_TAX_D", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal normalReturnAmountIncludingTaxD = BigDecimal.ZERO;
	/**
	 * The totalNormalSaleTaxAmount
	 */
	@Column(name = "TOTAL_NORMAL_SALE_TAX_AMOUNT", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal totalNormalSaleTaxAmount = BigDecimal.ZERO;
	/**
	 * The totalNormalSaleAmountIncludingTax
	 */
	@Column(name = "TOTAL_NORMAL_SALE_AMOUNT_INCLUDING_TAX", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal totalNormalSaleAmountIncludingTax = BigDecimal.ZERO;
	/**
	 * The totalNormalReturnTaxAmount
	 */
	@Column(name = "TOTAL_NORMAL_RETURN_TAX_AMOUNT", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal totalNormalReturnTaxAmount = BigDecimal.ZERO;
	/**
	 * The totalNormalReturnAmountIncludingTax
	 */
	@Column(name = "TOTAL_NORMAL_RETURN_AMOUNT_INCLUDING_TAX", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal totalNormalReturnAmountIncludingTax = BigDecimal.ZERO;
	/**
	 * The totalTrainingInvoiceNumber
	 */
	@Column(name = "TOTAL_TRAINING_INVOICE_NUMBER", nullable = true, columnDefinition = "integer default 0")
	@Builder.Default
	private Integer totalTrainingInvoiceNumber = 0;
	/**
	 * The trainingSaleAmountIncludingTaxA
	 */
	@Column(name = "TRAINING_SALE_AMOUNT_INCLUDING_TAX_A", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal trainingSaleAmountIncludingTaxA = BigDecimal.ZERO;
	/**
	 * The trainingSaleAmountIncludingTaxB1
	 */
	@Column(name = "TRAINING_SALE_AMOUNT_INCLUDING_TAX_B1", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal trainingSaleAmountIncludingTaxB1 = BigDecimal.ZERO;
	/**
	 * The trainingSaleAmountIncludingTaxB2
	 */
	@Column(name = "TRAINING_SALE_AMOUNT_INCLUDING_TAX_B2", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal trainingSaleAmountIncludingTaxB2 = BigDecimal.ZERO;
	/**
	 * The trainingSaleAmountIncludingTaxC
	 */
	@Column(name = "TRAINING_SALE_AMOUNT_INCLUDING_TAX_C", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal trainingSaleAmountIncludingTaxC = BigDecimal.ZERO;
	/**
	 * The trainingSaleAmountIncludingTaxD
	 */
	@Column(name = "TRAINING_SALE_AMOUNT_INCLUDING_TAX_D", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal trainingSaleAmountIncludingTaxD = BigDecimal.ZERO;
	/**
	 * The trainingReturnAmountIncludingTaxA
	 */
	@Column(name = "TRAINING_RETURN_AMOUNT_INCLUDING_TAX_A", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal trainingReturnAmountIncludingTaxA = BigDecimal.ZERO;
	/**
	 * The trainingReturnAmountIncludingTaxB1
	 */
	@Column(name = "TRAINING_RETURN_AMOUNT_INCLUDING_TAX_B1", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal trainingReturnAmountIncludingTaxB1 = BigDecimal.ZERO;
	/**
	 * The trainingReturnAmountIncludingTaxB2
	 */
	@Column(name = "TRAINING_RETURN_AMOUNT_INCLUDING_TAX_B2", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal trainingReturnAmountIncludingTaxB2 = BigDecimal.ZERO;
	/**
	 * The trainingReturnAmountIncludingTaxC
	 */
	@Column(name = "TRAINING_RETURN_AMOUNT_INCLUDING_TAX_C", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal trainingReturnAmountIncludingTaxC = BigDecimal.ZERO;
	/**
	 * The trainingReturnAmountIncludingTaxD
	 */
	@Column(name = "TRAINING_RETURN_AMOUNT_INCLUDING_TAX_D", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal trainingReturnAmountIncludingTaxD = BigDecimal.ZERO;
	/**
	 * The totalTrainingSaleTaxAmount
	 */
	@Column(name = "TOTAL_TRAINING_SALE_TAX_AMOUNT", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal totalTrainingSaleTaxAmount = BigDecimal.ZERO;
	/**
	 * The totalTrainingSaleAmountExcludingTax
	 */
	@Column(name = "TOTAL_TRAINING_SALE_AMOUNT_EXCLUDING_TAX", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal totalTrainingSaleAmountExcludingTax = BigDecimal.ZERO;
	/**
	 * The totalTrainingSaleAmountIncludingTax
	 */
	@Column(name = "TOTAL_TRAINING_SALE_AMOUNT_INCLUDING_TAX", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal totalTrainingSaleAmountIncludingTax = BigDecimal.ZERO;
	/**
	 * The totalTrainingReturnTaxAmount
	 */
	@Column(name = "TOTAL_TRAINING_RETURN_TAX_AMOUNT", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal totalTrainingReturnTaxAmount = BigDecimal.ZERO;
	/**
	 * The totalTrainingReturnAmountExcludingTax
	 */
	@Column(name = "TOTAL_TRAINING_RETURN_AMOUNT_EXCLUDING_TAX", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal totalTrainingReturnAmountExcludingTax = BigDecimal.ZERO;
	/**
	 * The totalTrainingReturnAmountIncludingTax
	 */
	@Column(name = "TOTAL_TRAINING_RETURN_AMOUNT_INCLUDING_TAX", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal totalTrainingReturnAmountIncludingTax = BigDecimal.ZERO;
	/**
	 * The totalCopyInvoiceNumber
	 */
	@Column(name = "TOTAL_COPY_INVOICE_NUMBER", nullable = true, columnDefinition = "integer default 0")
	@Builder.Default
	private Integer totalCopyInvoiceNumber = 0;
	/**
	 * The copySaleAmountIncludingTaxA
	 */
	@Column(name = "COPY_SALE_AMOUNT_INCLUDING_TAX_A", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal copySaleAmountIncludingTaxA = BigDecimal.ZERO;
	/**
	 * The copySaleAmountIncludingTaxB1
	 */
	@Column(name = "COPY_SALE_AMOUNT_INCLUDING_TAX_B1", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal copySaleAmountIncludingTaxB1 = BigDecimal.ZERO;
	/**
	 * The copySaleAmountIncludingTaxB2
	 */
	@Column(name = "COPY_SALE_AMOUNT_INCLUDING_TAX_B2", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal copySaleAmountIncludingTaxB2 = BigDecimal.ZERO;
	/**
	 * The copySaleAmountIncludingTaxC
	 */
	@Column(name = "COPY_SALE_AMOUNT_INCLUDING_TAX_C", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal copySaleAmountIncludingTaxC = BigDecimal.ZERO;
	/**
	 * The copySaleAmountIncludingTaxDS
	 */
	@Column(name = "COPY_SALE_AMOUNT_INCLUDING_TAX_D", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal copySaleAmountIncludingTaxD = BigDecimal.ZERO;
	/**
	 * The copyReturnAmountIncludingTaxA
	 */
	@Column(name = "COPY_RETURN_AMOUNT_INCLUDING_TAX_A", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal copyReturnAmountIncludingTaxA = BigDecimal.ZERO;
	/**
	 * The copyReturnAmountIncludingTaxB1
	 */
	@Column(name = "COPY_RETURN_AMOUNT_INCLUDING_TAX_B1", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal copyReturnAmountIncludingTaxB1 = BigDecimal.ZERO;
	/**
	 * The copyReturnAmountIncludingTaxB2
	 */
	@Column(name = "COPY_RETURN_AMOUNT_INCLUDING_TAX_B2", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal copyReturnAmountIncludingTaxB2 = BigDecimal.ZERO;
	/**
	 * The copyReturnAmountIncludingTaxC
	 */
	@Column(name = "COPY_RETURN_AMOUNT_INCLUDING_TAX_C", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal copyReturnAmountIncludingTaxC = BigDecimal.ZERO;
	/**
	 * The copyReturnAmountIncludingTaxD
	 */
	@Column(name = "COPY_RETURN_AMOUNT_INCLUDING_TAX_D", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal copyReturnAmountIncludingTaxD = BigDecimal.ZERO;
	/**
	 * The totalCopySaleTaxAmount
	 */
	@Column(name = "TOTAL_COPY_SALE_TAX_AMOUNT", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal totalCopySaleTaxAmount = BigDecimal.ZERO;
	/**
	 * The totalCopySaleAmountExcludingTax
	 */
	@Column(name = "TOTAL_COPY_SALE_AMOUNT_EXCLUDING_TAX", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal totalCopySaleAmountExcludingTax = BigDecimal.ZERO;
	/**
	 * The totalCopySaleAmountIncludingTax
	 */
	@Column(name = "TOTAL_COPY_SALE_AMOUNT_INCLUDING_TAX", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal totalCopySaleAmountIncludingTax = BigDecimal.ZERO;
	/**
	 * The totalCopyReturnTaxAmount
	 */
	@Column(name = "TOTAL_COPY_RETURN_TAX_AMOUNT", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal totalCopyReturnTaxAmount = BigDecimal.ZERO;
	/**
	 * The totalCopyReturnAmountExcludingTax
	 */
	@Column(name = "TOTAL_COPY_RETURN_AMOUNT_EXCLUDING_TAX", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal totalCopyReturnAmountExcludingTax = BigDecimal.ZERO;
	/**
	 * The totalCopyReturnAmountIncludingTax
	 */
	@Column(name = "TOTAL_COPY_RETURN_AMOUNT_INCLUDING_TAX", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal totalCopyReturnAmountIncludingTax = BigDecimal.ZERO;
	/**
	 * The totalProformaInvoiceNumber
	 */
	@Column(name = "TOTAL_PROFORMA_INVOICE_NUMBER", nullable = true, columnDefinition = "integer default 0")
	@Builder.Default
	private Integer totalProformaInvoiceNumber = 0;
	/**
	 * The proformaSaleAmountIncludingTaxA
	 */
	@Column(name = "PROFORMA_SALE_AMOUNT_INCLUDING_TAX_A", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal proformaSaleAmountIncludingTaxA = BigDecimal.ZERO;
	/**
	 * The proformaSaleAmountIncludingTaxB1
	 */
	@Column(name = "PROFORMA_SALE_AMOUNT_INCLUDING_TAX_B1", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal proformaSaleAmountIncludingTaxB1 = BigDecimal.ZERO;
	/**
	 * The proformaSaleAmountIncludingTaxB2
	 */
	@Column(name = "PROFORMA_SALE_AMOUNT_INCLUDING_TAX_B2", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal proformaSaleAmountIncludingTaxB2 = BigDecimal.ZERO;
	/**
	 * The proformaSaleAmountIncludingTaxC
	 */
	@Column(name = "PROFORMA_SALE_AMOUNT_INCLUDING_TAX_C", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal proformaSaleAmountIncludingTaxC = BigDecimal.ZERO;
	/**
	 * The proformaSaleAmountIncludingTaxD
	 */
	@Column(name = "PROFORMA_SALE_AMOUNT_INCLUDING_TAX_D", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal proformaSaleAmountIncludingTaxD = BigDecimal.ZERO;
	/**
	 * The proformaReturnAmountIncludingTaxA
	 */
	@Column(name = "PROFORMA_RETURN_AMOUNT_INCLUDING_TAX_A", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal proformaReturnAmountIncludingTaxA = BigDecimal.ZERO;
	/**
	 * The proformaReturnAmountIncludingTaxB1
	 */
	@Column(name = "PROFORMA_RETURN_AMOUNT_INCLUDING_TAX_B1", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal proformaReturnAmountIncludingTaxB1 = BigDecimal.ZERO;
	/**
	 * The proformaReturnAmountIncludingTaxB2
	 */
	@Column(name = "PROFORMA_RETURN_AMOUNT_INCLUDING_TAX_B2", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal proformaReturnAmountIncludingTaxB2 = BigDecimal.ZERO;
	/**
	 * The proformaReturnAmountIncludingTaxC
	 */
	@Column(name = "PROFORMA_RETURN_AMOUNT_INCLUDING_TAX_C", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal proformaReturnAmountIncludingTaxC = BigDecimal.ZERO;
	/**
	 * The proformaReturnAmountIncludingTaxD
	 */
	@Column(name = "PROFORMA_RETURN_AMOUNT_INCLUDING_TAX_D", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal proformaReturnAmountIncludingTaxD = BigDecimal.ZERO;
	/**
	 * The totalProformaSaleTaxAmount
	 */
	@Column(name = "TOTAL_PROFORMA_SALE_TAX_AMOUNT", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal totalProformaSaleTaxAmount = BigDecimal.ZERO;
	/**
	 * The totalProformaSaleAmountExcludingTax
	 */
	@Column(name = "TOTAL_PROFORMA_SALE_AMOUNT_EXCLUDING_TAX", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal totalProformaSaleAmountExcludingTax = BigDecimal.ZERO;
	/**
	 * The totalProformaSaleAmountIncludingTax
	 */
	@Column(name = "TOTAL_PROFORMA_SALE_AMOUNT_INCLUDING_TAX", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal totalProformaSaleAmountIncludingTax = BigDecimal.ZERO;
	/**
	 * The totalProformaReturnTaxAmount
	 */
	@Column(name = "TOTAL_PROFORMA_RETURN_TAX_AMOUNT", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal totalProformaReturnTaxAmount = BigDecimal.ZERO;
	/**
	 * The totalProformaReturnAmountExcludingTax
	 */
	@Column(name = "TOTAL_PROFORMA_RETURN_AMOUNT_EXCLUDING_TAX", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal totalProformaReturnAmountExcludingTax = BigDecimal.ZERO;
	/**
	 * The totalProformaReturnAmountIncludingTax
	 */
	@Column(name = "TOTAL_PROFORMA_RETURN_AMOUNT_INCLUDING_TAX", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal totalProformaReturnAmountIncludingTax = BigDecimal.ZERO;
	/**
	 * The totalInvoiceNumber
	 */
	@Column(name = "TOTAL_INVOICE_NUMBER", nullable = true, columnDefinition = "integer default 0")
	@Builder.Default
	private Integer totalInvoiceNumber = 0;
	/**
	 * The totalSaleTaxAmount
	 */
	@Column(name = "TOTAL_SALE_TAX_AMOUNT", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal totalSaleTaxAmount = BigDecimal.ZERO;
	/**
	 * The totalReturnTaxAmount
	 */
	@Column(name = "TOTAL_RETURN_TAX_AMOUNT", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal totalReturnTaxAmount = BigDecimal.ZERO;
	/**
	 * The totalSaleAmountExcludeTax
	 */
	@Column(name = "TOTAL_SALE_AMOUNT_EXCLUDE_TAX", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal totalSaleAmountExcludeTax = BigDecimal.ZERO;
	/**
	 * The totalReturnAmountExcludeTax
	 */
	@Column(name = "TOTAL_RETURN_AMOUNT_EXCLUDE_TAX", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal totalReturnAmountExcludeTax = BigDecimal.ZERO;
	/**
	 * The totalSaleAmountIncludeTax
	 */
	@Column(name = "TOTAL_SALE_AMOUNT_INCLUDE_TAX", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal totalSaleAmountIncludeTax = BigDecimal.ZERO;
	/**
	 * The totalReturnAmountIncludeTax
	 */
	@Column(name = "TOTAL_RETURN_AMOUNT_INCLUDE_TAX", nullable = true, precision = 19, scale = 2)
	@Builder.Default
	private BigDecimal totalReturnAmountIncludeTax = BigDecimal.ZERO;
	/**
	 * The sdcInformation
	 */
	@ManyToOne
	@JoinColumn(name = "SDC_INFORMATION_ID", nullable = false)
	private SdcInformation sdcInformation;

}
