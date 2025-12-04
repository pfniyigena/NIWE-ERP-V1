package com.niwe.erp.inventory.domain;

import com.niwe.erp.common.domain.AbstractEntity;
import com.niwe.erp.core.domain.CoreBranch;
import com.niwe.erp.core.domain.CoreTaxpayer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
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
@Table(name = "INVENTORY_WAREHOUSE")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Warehouse extends AbstractEntity {

	/**
	 * The serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The internalCode
	 */
	@Column(name = "INTERNAL_CODE",unique = true, nullable = false)
	private String internalCode;
	/**
	 * The name
	 */
	@NotBlank
	@Column(name = "WAREHOUSE_NAME", unique = true,nullable = false)
	private String warehouseName; 
	/**
	 * The isMain
	 */
	@Column(name = "MAIN", nullable = false)
	private boolean isMain;
	/**
	 * The branch
	 */
	@ManyToOne
	@JoinColumn(name = "BRANCH_ID",nullable = false)
	private CoreBranch branch;
	
	/**
	 * The Taxpayer
	 */
	@ManyToOne
	@JoinColumn(name = "TAXPAYER_ID", nullable = false)
	private CoreTaxpayer taxpayer;
}
