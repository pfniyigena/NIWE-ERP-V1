package com.niwe.erp.core.web.ajax;

import java.util.List;

public record WarehouseStockDataTablesRequest(
	    int draw,
	    int start,
	    int length,
	    Search search,
	    List<Order> order,
	    List<Column> columns,
	    String categoryId,
	    String brandId
	) {}

	
