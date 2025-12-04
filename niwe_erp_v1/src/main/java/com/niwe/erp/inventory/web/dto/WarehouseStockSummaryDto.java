package com.niwe.erp.inventory.web.dto;

import java.math.BigDecimal;

import com.niwe.erp.inventory.domain.Warehouse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseStockSummaryDto {
	private Warehouse warehouse;
    private BigDecimal totalIn;
    private BigDecimal totalOut;
    private BigDecimal totalValue;
    private BigDecimal balance;
}
