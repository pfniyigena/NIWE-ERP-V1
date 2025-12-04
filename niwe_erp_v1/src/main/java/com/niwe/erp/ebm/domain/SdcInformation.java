package com.niwe.erp.ebm.domain;

import com.niwe.erp.common.domain.AbstractEntity;
import com.niwe.erp.core.domain.CoreBranch;
import com.niwe.erp.core.domain.CoreTaxpayer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "EBM_SDC_INFORMATION")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class SdcInformation extends AbstractEntity {
	/**
	 * The serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The sdcNumber
	 */
	@Column(name = "SDC_NUMBER", length = 12, nullable = false)
	private String sdcNumber;
	/**
	 * The serialNumber
	 */
	@Column(name = "SERIAL_NUMBER", nullable = false)
	private String serialNumber;
	 
	/**
	 * The internalDataKey
	 */
	@Column(name = "INTERNAL_DATA_KEY", nullable = false)
	private String internalDataKey;

	/**
	 * The signatureKey
	 */
	@Column(name = "SIGNATURE_KEY", nullable = false)
	private String signatureKey;

	/**
	 * The communicationKey
	 */
	@Column(name = "COMMUNICATION_KEY", nullable = false)
	private String communicationKey;
	
	/**
	 * The machineRegistrationCode
	 */
	@Column(name = "MACHINE_REGISTRATION_CODE", length = 11, nullable = true)
	private String machineRegistrationCode;
	/**
	 * The Taxpayer branch
	 */
	@ManyToOne
	@JoinColumn(name = "BRANCH_ID", nullable = false)
	private CoreBranch branch;
	
	@ManyToOne
	@JoinColumn(name = "TAXPAYER_ID", nullable = false)
	private CoreTaxpayer taxpayer;
}
