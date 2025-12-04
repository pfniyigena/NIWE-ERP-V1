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

import com.niwe.erp.core.service.CoreItemService;
import com.niwe.erp.core.view.CoreItemListView;
import com.niwe.erp.core.web.util.NiweErpCoreUrlConstants;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = NiweErpCoreUrlConstants.ITEMS_URL)
@AllArgsConstructor
public class ItemAjaxController {
	private final CoreItemService coreItemService;
	@PostMapping(value = "/ajax/data", consumes = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> getData(@RequestBody DataTablesRequest request) {

	    log.info("SEARCH VALUE RECEIVED: [{}]", request.search().value()); // ← NOW YOU WILL SEE IT!

	    String searchValue = request.search().value() == null ? "" : request.search().value().trim();

	    // Get sorting
	    String sortColumn = "itemName"; // default
	    String sortDir = "asc";
	    Pageable pageable = PageRequest.of(
	        request.start() / request.length(),
	        request.length(),
	        Sort.Direction.fromString(sortDir.toUpperCase()),
	        sortColumn
	    );

	    Page<CoreItemListView> itemsPage = coreItemService.getItems(pageable, searchValue, searchValue);
	    return Map.of(
	        "draw", request.draw(),
	        "recordsTotal", coreItemService.countAll(),
	        "recordsFiltered", itemsPage.getTotalElements(),
	        "data", itemsPage.getContent()
	    );
	}
}
