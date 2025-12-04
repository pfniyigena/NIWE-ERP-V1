package com.niwe.erp.inventory.web.view;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public interface LocationStockView {
    UUID getLocationId();
    BigDecimal getAvailableQty();
    Instant getCreatedAt(); // For FIFO sorting
}

