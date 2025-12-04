package com.niwe.erp.core.domain;

import com.niwe.erp.common.domain.AbstractEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
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
@Table(name = "CORE_TAXPAYER")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CoreTaxpayer extends AbstractEntity {

	/**
	 * The serial version
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The internalCode
	 */
	@Column(name = "INTERNAL_CODE", nullable = false, unique = true)
	private String internalCode;
	/**
	 * The tinNumber
	 */
	@Column(name = "TIN_NUMBER", nullable = false,unique = true)
	private String tinNumber;
	
	/**
	 * The taxpayerName
	 */
	@NotBlank
	@Column(name = "TAXPAYER_NAME", nullable = false)
	private String taxpayerName;
	
	/**
	 * The taxpayerEmail
	 */
	@Email
	@Column(name = "TAXPAYER_EMAIL", nullable = true)
	private String taxpayerEmail;

	/**
	 * The taxpayerPhoneNumber
	 */
	@Column(name = "TAXPAYER_PHONE_NUMBER", nullable = true)
	private String taxpayerPhoneNumber;

	/**
	 * The taxpayerAddress
	 */
	@Column(name = "TAXPAYER_ADDRESS", nullable = true)
	private String taxpayerAddress;
	
	/**
	 * The allowedToExport
	 */
	@Column(name = "ALLOWED_TO_EXPORT", nullable = false)
	@Builder.Default
	private Boolean allowedToExport = Boolean.FALSE;
	
	/**
	 * The vatRegistered
	 */
	@Column(name = "VAT_REGISTERED", nullable = false)
	@Builder.Default
	private Boolean vatRegistered = Boolean.TRUE;
	
	/**
	 * The deboursRegistered
	 */
	@Column(name = "DEBOURS_REGISTERED", nullable = false)
	@Builder.Default
	private Boolean deboursRegistered = Boolean.FALSE;
	
	/**
	 * The communalTaxRegistered
	 */
	@Column(name = "COMMUNAL_TAX_REGISTERED", nullable = false)
	@Builder.Default
	private Boolean communalTaxRegistered = Boolean.FALSE;
	
	/**
	 * The fiscalYear
	 */
	@Column(name = "FISCAL_YEAR", nullable = true)
	@lombok.ToString.Include
	private EFiscalYear fiscalYear;
	/**
	 * The useInvoice
	 */
	@Column(name = "USE_INVENTORY", nullable = false)
	@Builder.Default
	private boolean useInventory = false;
	
	/**
	 * The isDefault
	 */
	@Column(name = "IS_DEFAULT", nullable = false)
	@Builder.Default
	private boolean isDefault=Boolean.FALSE;
}
