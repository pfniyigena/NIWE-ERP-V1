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
@Table(name = "INVOICING_TAX_RATE")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class TaxType extends AbstractEntity {
	/**
	 * The serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The taxName
	 */
	@NotBlank
	@Column(name = "TAX_NAME", nullable = false,unique = true)
	private String taxName;
	
	/**
	 * The displayName
	 */
	@Column(name = "DISPLAY_NAME", nullable = true)
	private String displayName;
	
	/**
	 * The taxCode
	 */
	@NotBlank
	@Column(name = "TAX_CODE", nullable = false,unique = true)
	private String taxCode;
	/**
	 * The taxValue
	 */
	@Column(name = "TAX_VALUE", nullable = true)
	@Builder.Default
	private BigDecimal taxValue = BigDecimal.ZERO;
	/**
	 * The description
	 */
	@Column(name = "DESCRIPTION", nullable = true)
	private String description;
	
	@Column(name = "DISPLAY_LEVEL",unique =true, nullable = true)
	@Builder.Default
	private Integer displayLevel=0;

	/**
	 * The isDefault
	 */
	@Column(name = "IS_DEFAULT", nullable = false)
	@Builder.Default
	private Boolean isDefault=Boolean.FALSE;

}
