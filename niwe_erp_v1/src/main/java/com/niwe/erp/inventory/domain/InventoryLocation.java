package com.niwe.erp.inventory.domain;

import com.niwe.erp.common.domain.AbstractEntity;

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
@Table(name = "INVENTORY_LOCATION")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class InventoryLocation extends AbstractEntity {

	/**
	 * The serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The internalCode
	 */
	@Column(name = "INTERNAL_CODE", unique = true, nullable = false)
	private String internalCode;
	/**
	 * The internalCode
	 */
	@Column(name = "LOCATION_CODE", unique = true, nullable = false)
	private String locationCode;
	/**
	 * The name
	 */
	@NotBlank
	@Column(name = "LOCATION_NAME", unique = true, nullable = false)
	private String locationName;
	/**
	 * The name
	 */

	@Column(name = "MANAGER_NAME")
	private String managerName;
	/**
	 * The isMain
	 */
	@Column(name = "PRIORITY")
	private int priority;

	/**
	 * The Taxpayer
	 */
	@ManyToOne
	@JoinColumn(name = "WAREHOUSE_ID", nullable = false)
	private Warehouse warehouse;
}
