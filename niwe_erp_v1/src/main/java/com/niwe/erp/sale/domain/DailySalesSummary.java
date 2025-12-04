package com.niwe.erp.sale.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.niwe.erp.common.domain.AbstractEntity;
import com.niwe.erp.core.domain.CoreTaxpayer;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "REPORT_DAILY_SALES_SUMMARY")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class DailySalesSummary extends AbstractEntity{
	
	/**
	 * The serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The totalDiscountAmount
	 */
	@Column(name = "TOTAL_DISCOUNT_AMOUNT", nullable = true)
	@Builder.Default
	private BigDecimal totalDiscountAmount = BigDecimal.ZERO;
	/**
	 * The totlaTaxAmount
	 */
	@Column(name = "TOTAL_TAX_AMOUNT", nullable = true)
	@Builder.Default
	private BigDecimal totalTaxAmount = BigDecimal.ZERO;
	/**
	 * The totalAmountInclusiveTax
	 */
	@Column(name = "TOTAL_AMOUNT_INCLUSIVE_TAX", nullable = true)
	@Builder.Default
	private BigDecimal totalAmountInclusiveTax = BigDecimal.ZERO;
	/**
	 * The totalAmountHorsTax
	 */
	@Column(name = "TOTAL_AMOUNT_HORS_TAX", nullable = true)
	@Builder.Default
	private BigDecimal totalAmountHorsTax = BigDecimal.ZERO;
	/**
	 * The totalGrossAmount
	 */
	@Column(name = "TOTAL_GROSS_AMOUNT", nullable = true)
	@Builder.Default
	private BigDecimal totalGrossAmount = BigDecimal.ZERO;
	/**
	 * The totalExtraAmount
	 */
	@Column(name = "TOTAL_EXTRA_AMOUNT", nullable = true)
	@Builder.Default
	private BigDecimal totalExtraAmount = BigDecimal.ZERO;

	/**
	 * The totalAmountToPay
	 */
	@Column(name = "TOTAL_AMOUNT_TO_PAY", nullable = true)
	@Builder.Default
	private BigDecimal totalAmountToPay = BigDecimal.ZERO;
	/**
	 * The totalPurchaseAmountInclusiveTax
	 */
	@Column(name = "TOTAL_COST")
	@Builder.Default
	private BigDecimal totalCost= BigDecimal.ZERO;

	/**
	 * The costAmount
	 */
	@Column(name = "TOTAL_PROFIT")
	@Builder.Default
	private BigDecimal totalProfit= BigDecimal.ZERO;

	/**
	 * The numberOfReceipts
	 */
	@Column(name = "NUMBER_OF_RECEIPTS")
	@Builder.Default
	private Integer numberOfReceipts=0;
	
	/**
	 * The summaryDate
	 */
	@Column(name = "SUMMARY_DATE")
	private LocalDate summaryDate;

	/**
	 * The items
	 */
	@OneToMany(mappedBy = "summary", cascade = CascadeType.ALL)
	@Builder.Default
	@ToString.Exclude
	private List<DailySalesSummaryPayment> payments = new ArrayList<>();
	

	/**
	 * The Taxpayer
	 */
	@ManyToOne
	@JoinColumn(name = "TAXPAYER_ID", nullable = false)
	private CoreTaxpayer taxpayer;
}
