package com.niwe.erp.inventory.web.form;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class StockTransferForm {
	private UUID id;
	private String fromWarehouseId;
	private String fromWarehouseCode;
	private String fromWarehouseName;
	private String toWarehouseId;
	private String toWarehouseCode;
	private String toWarehouseName;
	@Builder.Default
	private List<StockTransferLineForm> lines = new ArrayList<>();
	 
}
