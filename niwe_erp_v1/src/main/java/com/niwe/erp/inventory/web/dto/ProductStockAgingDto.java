package com.niwe.erp.inventory.web.dto;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductStockAgingDto{
	private UUID itemId;
    private String productCode;
    private String productName;
    private BigDecimal totalQuantity;
    private Map<String, BigDecimal> agingBuckets;
	
}
