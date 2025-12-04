package com.niwe.erp.inventory.web.view;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public interface StockMovementListView {
	/**
	 * @return
	 */
	UUID getItemId();

	/**
	 * @return
	 */
	String getItemName();

	/**
	 * @return
	 */
	String getItemCode();

	/**
	 * @return
	 */
	String getBarcode();

	/**
	 * @return
	 */
	BigDecimal getUnitPrice();

	BigDecimal getUnitCost();

	UUID getWarehouseId();

	UUID getLocationId();

	Instant getMovementDate();

	BigDecimal getMovedQuantity();

	BigDecimal getPrevWarehouseQuantity();

	BigDecimal getCurrentWarehouseQuantity();

	BigDecimal getPrevLocationQuantity();

	BigDecimal getCurrentLocationQuantity();

	String getMovementType();

}
