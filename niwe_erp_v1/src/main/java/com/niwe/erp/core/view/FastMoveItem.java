package com.niwe.erp.core.view;
 

import java.math.BigDecimal;
import java.time.Instant;

public interface FastMoveItem{
        String getItemName();
        BigDecimal getSold();
        Instant getModifiedAt();
}
