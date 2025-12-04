package com.niwe.erp.inventory.web.view;
 

import java.math.BigDecimal;
import java.util.UUID;

public interface InflowItemListView{
        UUID getItemId();
        String getItemName();
        String getItemCode();
        String getBarcode();
        BigDecimal getUnitPrice();
        BigDecimal getUnitCost();
        BigDecimal getQuantity();
        UUID getWarehouseId();
}
