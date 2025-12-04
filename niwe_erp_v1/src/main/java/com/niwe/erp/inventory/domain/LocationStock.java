package com.niwe.erp.inventory.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.niwe.erp.common.domain.AbstractEntity;
import com.niwe.erp.core.domain.CoreItem;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
@Table(name = "INVENTORY_LOCATION_STOCK", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "LOCATION_ID", "ITEM_ID" }) })
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class LocationStock extends AbstractEntity {
	/**
	 * The serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The location
	 */

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "LOCATION_ID")
	private InventoryLocation location;

	/**
	 * The item
	 */
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "ITEM_ID")
	private CoreItem item;
	/**
	 * The quantity
	 */
	@Column(name = "QUANTITY")
	private BigDecimal quantity;
	/**
	 * The lastUpdated
	 */
	@Column(name = "LAST_UPDATED")
    private LocalDateTime lastUpdated;

}
