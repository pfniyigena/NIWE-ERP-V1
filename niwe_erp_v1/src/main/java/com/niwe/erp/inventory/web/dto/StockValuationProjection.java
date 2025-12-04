package com.niwe.erp.inventory.web.dto;

import java.math.BigDecimal;
import java.util.UUID;

public interface StockValuationProjection {
    UUID getItemId();
    String getItemCode();
    String getItemName();
    BigDecimal getTotalQuantity();
    BigDecimal getUnitCost();
}

