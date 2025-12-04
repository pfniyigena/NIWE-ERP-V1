package com.niwe.erp.inventory.domain;

import java.math.BigDecimal;
import java.time.Instant;

import com.niwe.erp.common.domain.AbstractEntity;
import com.niwe.erp.core.domain.CoreItem;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "INVENTORY_STOCK_MOVEMENT")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class StockMovement extends AbstractEntity {

	/**
	 * The serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The reference
	 */
	@Column(name = "REFERENCE")
	private String reference;
	
	/**
	 * The quantity
	 */
	@Column(name = "MOVED_QUANTITY")
	@Builder.Default
	private BigDecimal movedQuantity=BigDecimal.ZERO;;
	
	/**
	 * The movementType
	 */
	@Column(name = "MOVEMENT_TYPE")
	@Enumerated(EnumType.STRING)
	@lombok.ToString.Include
	private MovementType movementType;

	/**
	 * The movementDate
	 */
	@Column(name = "MOVEMENT_DATE")
	@Builder.Default
	private Instant movementDate = Instant.now();

	/**
	 * The item
	 */
	@ManyToOne
	@JoinColumn(name = "ITEM_ID", nullable = false)
	private CoreItem item;

	/**
	 * The fromWarehouse
	 */
	@ManyToOne
	@JoinColumn(name = "FROM_WAREHOUSE_ID")
	private Warehouse fromWarehouse;
	
	/**
	 * The fromWarehouse
	 */
	@ManyToOne
	@JoinColumn(name = "TO_WAREHOUSE_ID")
	private Warehouse toWarehouse;

	/**
	 * The fromLocation
	 */
	@ManyToOne
	@JoinColumn(name = "FROM_LOCATION_ID")
	private InventoryLocation fromLocation;
	
	/**
	 * The fromLocation
	 */
	@ManyToOne
	@JoinColumn(name = "TO_LOCATION_ID")
	private InventoryLocation toLocation;
	/**
	 * The previousWarehouseQuantity
	 */
	@Column(name = "PREVIOUS_WAREHOUSE_QUANTITY")
	@Builder.Default
	private BigDecimal previousWarehouseQuantity=BigDecimal.ZERO;

	/**
	 * The currentWarehouseQuantity
	 */
	@Column(name = "CURRENT_WAREHOUSE_QUANTITY")
	@Builder.Default
	private BigDecimal currentWarehouseQuantity=BigDecimal.ZERO;
	/**
	 * The previousWarehouseQuantity
	 */
	@Column(name = "PREVIOUS_LOCATION_QUANTITY")
	@Builder.Default
	private BigDecimal previousLocationQuantity=BigDecimal.ZERO;

	/**
	 * The currentWarehouseQuantity
	 */
	@Column(name = "CURRENT_LOCATION_QUANTITY")
	@Builder.Default
	private BigDecimal currentLocationQuantity=BigDecimal.ZERO;
}
