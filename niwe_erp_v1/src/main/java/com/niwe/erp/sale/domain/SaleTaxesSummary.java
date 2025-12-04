package com.niwe.erp.sale.domain;

import java.math.BigDecimal;

import com.niwe.erp.common.domain.AbstractEntity;
import com.niwe.erp.invoicing.domain.TaxType;

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
@Table(name = "SALE_TAXES_SUMMARY")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class SaleTaxesSummary extends AbstractEntity {

	/**
	 * The serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The taxCode
	 */
	@Column(name = "TAX_CODE", nullable = false)
	private String taxCode;

	/**
	 * The taxName
	 */
	@Column(name = "TAX_NAME", nullable = false)
	private String taxName;
	/**
	 * The taxValue
	 */
	@Column(name = "TAX_VALUE", nullable = true)
	@Builder.Default
	private BigDecimal taxValue = BigDecimal.ZERO;
	
	/**
	 * The totalAmount
	 */
	@Column(name = "TOTAL_AMOUNT", nullable = true)
	@Builder.Default
	private BigDecimal totalAmount = BigDecimal.ZERO;

	/**
	 * The tax
	 */
	@ManyToOne
	@JoinColumn(name = "TAX_TYPE_ID", nullable = false)
	private TaxType tax;
	/**
	 * The invoice
	 */
	@ManyToOne
	@JoinColumn(name = "SALE_ID", nullable = false)
	private Sale sale;

}
