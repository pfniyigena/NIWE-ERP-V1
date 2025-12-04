package com.niwe.erp.sale.domain;

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
@Table(name = "REPORT_DAILY_SALES_SUMMARY_PAYMENT")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class DailySalesSummaryPayment extends AbstractEntity{/**
	 * The serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The grossProfit
	 */
	@Column(name = "TOTAL_PAID_AMOUNT")
	@Builder.Default
	private BigDecimal totalPaidAmount=BigDecimal.ZERO;
	/**
	 * The numberOfReceipts
	 */
	@Column(name = "NUMBER_OF_TRANSACTIONS")
	@Builder.Default
	private Integer numberOfTransactions=0;
	/**
	 * The summary
	 */
	@ManyToOne
	@JoinColumn(name = "PAYMENT_METHO_ID")
	private PaymentMethod paymentMethod;
	/**
	 * The summary
	 */
	@ManyToOne
	@JoinColumn(name = "SUMMARY_ID")
	private DailySalesSummary summary;
}
