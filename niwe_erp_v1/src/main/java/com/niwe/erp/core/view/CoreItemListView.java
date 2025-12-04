package com.niwe.erp.core.view;
 

import java.math.BigDecimal;
import java.util.UUID;

public record CoreItemListView(
        UUID itemId,
        String itemName,
        String itemCode,
        String barcode,
        BigDecimal unitPrice,
        BigDecimal unitCost,
        UUID taxId,
        String taxCode,
        BigDecimal taxValue,
        UUID natureId,
        UUID classificationId
) {}
