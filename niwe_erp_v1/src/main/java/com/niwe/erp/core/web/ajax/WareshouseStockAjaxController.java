package com.niwe.erp.core.web.ajax;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.niwe.erp.inventory.service.WarehouseStockService;
import com.niwe.erp.inventory.web.dto.ProductStockValuationDto;
import com.niwe.erp.inventory.web.util.NikaErpInventoryUrlConstants;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = NikaErpInventoryUrlConstants.WAREHOUSE_STOCKS_URL)
@AllArgsConstructor
public class WareshouseStockAjaxController {
	private final WarehouseStockService service;

	@PostMapping(value = "/evaluation/data", consumes = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> getData(@RequestBody DataTablesRequest request) {

	    log.info("SEARCH VALUE RECEIVED: [{}]", request.search().value()); // ← NOW YOU WILL SEE IT!

	    String searchValue = request.search().value() == null ? "" : request.search().value().trim();

	    // Get sorting
	    String sortColumn = "item.itemName"; // default
	    String sortDir = "asc";
		/*
		 * if (!request.order().isEmpty()) { int colIndex =
		 * request.order().get(0).column(); sortColumn =
		 * request.columns().get(colIndex).data(); // e.g. "itemName" sortDir =
		 * request.order().get(0).dir(); }
		 */

	    Pageable pageable = PageRequest.of(
	        request.start() / request.length(),
	        request.length(),
	        Sort.Direction.fromString(sortDir.toUpperCase()),
	        sortColumn
	    );

	    Page<ProductStockValuationDto> page = service.getItems(pageable, searchValue, searchValue);

	    return Map.of(
	        "draw", request.draw(),
	        "recordsTotal", service.countAll(),
	        "recordsFiltered", page.getTotalElements(),
	        "data", page.getContent()
	    );
	}
}