package com.niwe.erp.invoicing.domain;

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
@Table(name = "INVOICING_CHARGE_SUMMARY")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class InvoiceChargeSummary extends AbstractEntity{
	/**
	 * The serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The chargeCode
	 */
	@Column(name = "CHARGE_CODE", nullable = false,unique = true)
	private String chargeCode;

	/**
	 * The chargeName
	 */
	@Column(name = "CHARGE_NAME", nullable = false,unique = true)
	private String chargeName;
	/**
	 * The taxValue
	 */
	@Column(name = "CHARGE_VALUE", nullable = true)
	@Builder.Default
	private BigDecimal chargeValue = BigDecimal.ZERO;
	/**
	 * The totalAmount
	 */
	@Column(name = "TOTAL_AMOUNT", nullable = true)
	@Builder.Default
	private BigDecimal totalAmount = BigDecimal.ZERO;
	/**
	 * The charge
	 */
	@ManyToOne
	@JoinColumn(name = "CHARGE_TYPE_ID", nullable = false)
	private ChargeType charge;
	/**
	 * The invoice
	 */
	@ManyToOne
	@JoinColumn(name = "INVOICE_ID", nullable = false)
	private Invoice invoice;

}
