package com.niwe.erp.sale.domain;

import java.math.BigDecimal;

import com.niwe.erp.common.domain.AbstractEntity;
import com.niwe.erp.core.domain.CoreItem;

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
@Table(name = "SALE_SALE_ITEM")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class SaleItem extends AbstractEntity {
	@Column(name = "ITEM_SEQ", nullable = false)
	@Builder.Default
	private Integer itemSeq = 0;
	/**
	 * The serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The itemName
	 */
	@Column(name = "ITEM_NAME")
	private String itemName;
	/**
	 * The quantity
	 */
	@Column(name = "QUANTITY")
	@Builder.Default
	private BigDecimal quantity= BigDecimal.ZERO;
	/**
	 * The salePrice
	 */
	@Column(name = "SALE_PRICE")
	@Builder.Default
	private BigDecimal salePrice= BigDecimal.ZERO;
	/**
	 * The purchasePrice
	 */
	@Column(name = "PURCHASE_PRICE")
	@Builder.Default
	private BigDecimal purchasePrice= BigDecimal.ZERO;
	
	/**
	 * The taxRate
	 */
	@Column(name = "TAX_RATE")
	@Builder.Default
	private BigDecimal taxRate= BigDecimal.ZERO;
	

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
	 * The taxAmount
	 */
	@Column(name = "TAX_AMOUNT", nullable = true)
	@Builder.Default
	private BigDecimal taxAmount = BigDecimal.ZERO;
	/**
	 * The amountInclusiveTax
	 */
	@Column(name = "AMOUNT_INCLUSIVE_TAX", nullable = true)
	@Builder.Default
	private BigDecimal amountInclusiveTax = BigDecimal.ZERO;
	/**
	 * The amountInclusiveTax
	 */
	@Column(name = "AMOUNT_HORS_TAX", nullable = true)
	@Builder.Default
	private BigDecimal amountHorsTax = BigDecimal.ZERO;
	/**
	 * The grossAmount
	 */
	@Column(name = "GROSS_AMOUNT", nullable = true)
	@Builder.Default
	private BigDecimal grossAmount = BigDecimal.ZERO;
	/**
	 * The extraAmount
	 */
	@Column(name = "EXTRA_AMOUNT", nullable = true)
	@Builder.Default
	private BigDecimal extraAmount = BigDecimal.ZERO;

	/**
	 * The amountToPay
	 */
	@Column(name = "AMOUNT_TO_PAY", nullable = true)
	@Builder.Default
	private BigDecimal amountToPay = BigDecimal.ZERO;

	/**
	 * The taxType
	 */
	@Column(name = "TAX_TYPE", nullable = true)
	@Builder.Default
	private BigDecimal taxType = BigDecimal.ZERO;
	/**
	 * The item
	 */
	@ManyToOne
	@JoinColumn(name = "ITEM_ID", nullable = false)
	private CoreItem item;
	/**
	 * The sale
	 */
	@ManyToOne
	@JoinColumn(name = "SALE_ID")
	private Sale sale;

}
