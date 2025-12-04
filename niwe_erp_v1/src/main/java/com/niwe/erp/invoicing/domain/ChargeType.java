package com.niwe.erp.invoicing.domain;

import java.math.BigDecimal;

import com.niwe.erp.common.domain.AbstractEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
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
@Table(name = "INVOICING_CHARGE_TYPE")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ChargeType extends AbstractEntity {
	/**
	 * The serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The taxName
	 */
	@NotBlank
	@Column(name = "CHARGE_NAME", nullable = false,unique = true)
	private String chargeName;
	
	/**
	 * The displayName
	 */
	@Column(name = "DISPLAY_NAME", nullable = true)
	private String displayName;
	
	/**
	 * The chargeCode
	 */
	@NotBlank
	@Column(name = "CHARGE_CODE", nullable = false,unique = true)
	private String chargeCode;
	/**
	 * The taxValue
	 */
	@Column(name = "CHARGE_VALUE", nullable = true)
	@Builder.Default
	private BigDecimal chargeValue = BigDecimal.ZERO;
	/**
	 * The description
	 */
	@Column(name = "DESCRIPTION", nullable = true)
	private String description;

	/**
	 * The description
	 */
	@Column(name = "inclusive", nullable = true)
	@Builder.Default
	private boolean inclusive=false;


}
