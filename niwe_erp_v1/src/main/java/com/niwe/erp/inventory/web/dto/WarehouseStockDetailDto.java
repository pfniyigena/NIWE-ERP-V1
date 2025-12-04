package com.niwe.erp.inventory.web.dto;


import java.math.BigDecimal;
import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseStockDetailDto {

    private String warehouseName;
    private BigDecimal quantity;
    private Instant lastUpdated;


}
