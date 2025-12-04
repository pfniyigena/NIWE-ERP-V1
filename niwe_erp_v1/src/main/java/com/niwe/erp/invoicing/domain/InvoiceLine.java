package com.niwe.erp.invoicing.domain;

import java.math.BigDecimal;

import com.niwe.erp.common.domain.AbstractEntity;
import com.niwe.erp.core.domain.CoreItem;
import com.niwe.erp.core.domain.EItemNature;

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
@Table(name = "INVOICING_INVOICE_LINE")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class InvoiceLine extends AbstractEntity {
	/**
	* The serialVersionUID
	*/
	private static final long serialVersionUID = 1L;
	/**
	 * The itemName
	 */
	@Column(name = "ITEM_NAME", nullable = false)
	private String itemName;
	/**
	 * The taxName
	 */
	@Column(name = "TAX_NAME", nullable = false)
	private String taxName;
	/**
	 * The unitPrice
	 */
	@Column(name = "UNIT_PRICE", nullable = true)
	@Builder.Default
	private BigDecimal unitPrice = BigDecimal.ZERO;
	/**
	 * The quantity
	 */
	@Column(name = "QUANTITY", nullable = true)
	@Builder.Default
	private BigDecimal quantity = BigDecimal.ZERO;
	/**
	 * The taxType
	 */
	@Column(name = "TAX_TYPE", nullable = true)
	@Builder.Default
	private BigDecimal taxType = BigDecimal.ZERO;
	/**
	 * The taxAmount
	 */
	@Column(name = "TAX_AMOUNT", nullable = true)
	@Builder.Default
	private BigDecimal taxAmount = BigDecimal.ZERO;
	/**
	 * The totalAmountInclusiveTax
	 */
	@Column(name = "TOTAL_AMOUNT_INCLUSIVE_TAX", nullable = true)
	@Builder.Default
	private BigDecimal totalAmountInclusiveTax = BigDecimal.ZERO;
	/**
	 * The discountRate
	 */
	@Column(name = "DISCOUNT_RATE", nullable = true)
	@Builder.Default
	private BigDecimal discountRate = BigDecimal.ZERO;
	/**
	 * The discountAmount
	 */
	@Column(name = "DISCOUNT_AMOUNT", nullable = true)
	@Builder.Default
	private BigDecimal discountAmount = BigDecimal.ZERO;
	/**
	 * The grossAmount
	 */
	@Column(name = "GROSS_AMOUNT", nullable = true)
	@Builder.Default
	private BigDecimal grossAmount = BigDecimal.ZERO;
	/**
	 * The transactionType
	 */
	@Column(name = "TRANSACTION_TYPE", nullable = false)
	@lombok.ToString.Include
	@Builder.Default
	private ETransactionType transactionType = ETransactionType.TRANSACTION_TYPE_SALE;
	/**
	 * The taxCode
	 */
	@Column(name = "TAX_CODE", nullable = false)
	private String taxCode;
	/**
	 * The item
	 */
	@ManyToOne
	@JoinColumn(name = "ITEM_ID", nullable = false)
	private CoreItem item;
	/**
	 * The itemNature
	 */
	@Column(name = "ITEM_NATURE", nullable = false)
	private EItemNature itemNature;
	@Column(name = "ITEM_SEQ", nullable = false)
	@Builder.Default
	private Integer itemSeq = 0;
	/**
	 * The pushedToFen
	 */
	@Column(name = "PUSHED_TO_FEN", nullable = false)
	@Builder.Default
	private Boolean pushedToFen = Boolean.FALSE;
	/**
	 * The irppRate
	 */
	@Column(name = "IRPP_RATE", nullable = true)
	@Builder.Default
	private BigDecimal irppRate = BigDecimal.ZERO;
	/**
	 * The irppAmount
	 */
	@Column(name = "IRPP_AMOUNT", nullable = true)
	@Builder.Default
	private BigDecimal irppAmount = BigDecimal.ZERO;
	/**
	 * The totalExtraAmount
	 */
	@Column(name = "TOTAL_EXTRA_AMOUNT", nullable = true)
	@Builder.Default
	private BigDecimal totalExtraAmount = BigDecimal.ZERO;
	/**
	 * The totalAPayer
	 */
	@Column(name = "TOTAL_A_PAYER", nullable = true)
	@Builder.Default
	private BigDecimal totalAPayer = BigDecimal.ZERO;
	/**
	 * The invoice
	 */
	@ManyToOne
	@JoinColumn(name = "INVOICE_ID", nullable = false)
	private Invoice invoice;

}
