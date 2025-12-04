package com.niwe.erp.inventory.web.view;

import java.math.BigDecimal;
import java.util.UUID;

public interface OutflowItemListView {
	UUID getItemId();

	String getItemName();

	String getItemCode();

	String getBarcode();

	BigDecimal getUnitPrice();

	BigDecimal getUnitCost();

	BigDecimal getQuantity();

	UUID getLocationId();

	UUID getWarehouseId();

	String getLocationCode();

	String getLocationName();

	Integer getPriority();
}
