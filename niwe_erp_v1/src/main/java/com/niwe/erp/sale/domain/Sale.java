package com.niwe.erp.sale.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.niwe.erp.common.domain.AbstractEntity;
import com.niwe.erp.common.domain.EChannel;
import com.niwe.erp.common.domain.PaymentStatus;
import com.niwe.erp.core.domain.CoreTaxpayer;
import com.niwe.erp.inventory.domain.Warehouse;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "SALE_SALE")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Sale extends AbstractEntity {
	/**
	 * The serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The itemNumber
	 */
	@Column(name = "ITEM_NUMBER", nullable = false)
	private Integer itemNumber;
	/**
	 * The internalCode
	 */
	@Column(name = "INTERNAL_CODE", nullable = false, unique = true)
	private String internalCode;
	/**
	 * The customerName
	 */
	@Column(name = "CUSTOMER_NAME")
	private String customerName;

	/**
	 * The customerName
	 */
	@Column(name = "CUSTOMER_TIN")
	private String customerTin;
	/**
	 * The customerName
	 */
	@Column(name = "CUSTOMER_PHONE")
	private String customerPhone;
	/**
	 * The confirmedBy
	 */
	@Column(name = "CONFIRMED_BY")
	private String confirmedBy;
	/**
	 * The saleDate
	 */
	@Column(name = "SALE_DATE")
	private Instant saleDate;

	/**
	 * The externalCode
	 */
	@Column(name = "EXTERNAL_CODE", nullable = true, length = 50)
	private String externalCode;
	/**
	 * The transactionType
	 */
	@Column(name = "TRANSACTION_TYPE", nullable = true, length = 50)
	@Enumerated(EnumType.STRING)
	private TransactionType transactionType;

	/**
	 * The status
	 */
	@Column(name = "SALE_STATUS", nullable = true)
	@Enumerated(EnumType.STRING)
	@Builder.Default
	private SaleStatus status = SaleStatus.DRAFT;
	/**
	 * The paymentMethos
	 */
	@ManyToOne
	@JoinColumn(name = "PAYMENT_METHOD_ID", nullable = true)
	private PaymentMethod paymentMethod;

	/**
	 * The paymentStatus
	 */
	@Column(name = "PAYMENT_STATUS", nullable = true)
	@Builder.Default
	private PaymentStatus paymentStatus = PaymentStatus.PENDING;
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
	private BigDecimal totalCost = BigDecimal.ZERO;
	/**
	 * The costAmount
	 */
	@Column(name = "TOTAL_PROFIT")
	@Builder.Default
	private BigDecimal totalProfit = BigDecimal.ZERO;

	/**
	 * The shelf
	 */
	@ManyToOne
	@JoinColumn(name = "SHELF_ID", nullable = true)
	private Shelf shelf;
	/**
	 * The Taxpayer
	 */
	@ManyToOne
	@JoinColumn(name = "TAXPAYER_ID", nullable = false)
	private CoreTaxpayer taxpayer;
	/**
	 * The summary
	 */
	@ManyToOne
	@JoinColumn(name = "DAILY_SUMMARY_ID", nullable = true)
	private DailySalesSummary summary;
	/**
	 * The items
	 */
	@OneToMany(mappedBy = "sale", cascade = CascadeType.ALL)
	@Builder.Default
	@ToString.Exclude
	private List<SaleItem> items = new ArrayList<>();
	/**
	 * The transactionType
	 */
	@Column(name = "SOURCE_CHANNEL", nullable = true, length = 50)
	@Builder.Default
	private EChannel sourceChannel = EChannel.WEB;
	/**
	 * The customer
	 */
	@ManyToOne
	@JoinColumn(name = "CUSTOMER_ID", nullable = true)
	private Customer customer;
	/**
	 * The warehouse
	 */
	@ManyToOne
	@JoinColumn(name = "WAREHOUSE_ID")
	private Warehouse warehouse;

}
