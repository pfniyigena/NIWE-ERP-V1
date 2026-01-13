package com.niwe.erp.core.view;
 

import java.math.BigDecimal;
import java.time.Instant;

public interface DeadStockItem{
        String getItemName();
        BigDecimal getSold();
        Instant getLastSold();
        Instant getModifiedAt();
}
