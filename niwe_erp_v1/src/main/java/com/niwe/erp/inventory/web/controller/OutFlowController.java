package com.niwe.erp.inventory.web.controller;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.niwe.erp.core.domain.CoreItem;
import com.niwe.erp.core.service.CoreItemService;
import com.niwe.erp.core.web.ajax.DataTablesRequest;
import com.niwe.erp.inventory.domain.InventoryLocation;
import com.niwe.erp.inventory.domain.Warehouse;
import com.niwe.erp.inventory.service.InventoryService;
import com.niwe.erp.inventory.service.LocationService;
import com.niwe.erp.inventory.service.LocationStockService;
import com.niwe.erp.inventory.service.StockMovementService;
import com.niwe.erp.inventory.service.WarehouseService;
import com.niwe.erp.inventory.web.util.NikaErpInventoryUrlConstants;
import com.niwe.erp.inventory.web.view.OutflowItemListView;
import com.niwe.erp.inventory.web.view.StockMovementListView;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = NikaErpInventoryUrlConstants.OUT_FLOWS_URL)
@AllArgsConstructor
public class OutFlowController {
	private final CoreItemService coreItemService;
	private final WarehouseService warehouseService;
	private final InventoryService inventoryService;
	private final LocationStockService locationStockService;
	private final LocationService locationService;
	private final StockMovementService stockMovementService;

	@GetMapping("/list")
	public String list(Model model) {
		Warehouse warehouse = warehouseService.findMain();
		model.addAttribute("warehouse", warehouse);
		return NikaErpInventoryUrlConstants.OUT_FLOWS_LIST_PAGE;
	}

	@PostMapping(value = "/list/data", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map<String, Object> getData(@RequestBody DataTablesRequest request) {

		Warehouse warehouse = warehouseService.findMain();
		log.info("SEARCH VALUE RECEIVED: [{}]", request.search().value()); // ← NOW YOU WILL SEE IT!
		String searchValue = request.search().value() == null ? "" : request.search().value().trim();
		// Get sorting
		String sortColumn = "itemName"; // default
		String sortDir = "asc";
		Pageable pageable = PageRequest.of(request.start() / request.length(), request.length(),
				Sort.Direction.fromString(sortDir.toUpperCase()), sortColumn);

		Page<OutflowItemListView> itemsPage = locationStockService.findAvailableStockByLocation(warehouse.getId(),
				searchValue, pageable);
		log.info("OUTFLOW DATA: [{}]", itemsPage.getTotalElements()); // ← NOW YOU WILL SEE IT!
		return Map.of("draw", request.draw(), "recordsTotal", coreItemService.countAll(), "recordsFiltered",
				itemsPage.getTotalElements(), "data", itemsPage.getContent(), "warehouse", warehouse);
	}

	@PostMapping("/save")
	public String transferWarehouseToLocation(@RequestParam String itemId, @RequestParam String warehouseId,
			@RequestParam String locationId, @RequestParam BigDecimal newValue, RedirectAttributes redirectAttributes) {
		log.info("transferWarehouseToLocation where warehouseId:{},locationId:{},itemId:{}", warehouseId, locationId,
				itemId);
		inventoryService.transferWarehouseToLocation(warehouseId, locationId, itemId, newValue, "");
		redirectAttributes.addFlashAttribute("success", "Success.");
		return NikaErpInventoryUrlConstants.OUT_FLOWS_LIST_REDITECT_URL;
	}

	@GetMapping(path = "/view/{itemId}/{warehouseId}/{locationId}")
	public String viewDetails(@PathVariable String itemId, @PathVariable String warehouseId,
			@PathVariable String locationId, Model model) {
		CoreItem item = coreItemService.findById(itemId);
		Warehouse warehouse = warehouseService.findById(warehouseId);
		InventoryLocation location = locationService.findById(locationId);
		model.addAttribute("item", item);
		model.addAttribute("itemId", item.getId());
		model.addAttribute("warehouse", warehouse);
		model.addAttribute("warehouseId", warehouse.getId());
		model.addAttribute("locationName", location.getLocationName().concat("-").concat(location.getInternalCode()));
		model.addAttribute("locationId", location.getId());
		return NikaErpInventoryUrlConstants.OUT_FLOWS_DETAILS_PAGE;
	}

	@PostMapping("/ajax/view/{itemId}/{locationId}")
	@ResponseBody
	public Map<String, Object> getItemLocationStock(@PathVariable UUID itemId, @PathVariable UUID locationId,
			@RequestBody DataTablesRequest request) {
		log.info("INFLOWS DATA: Item:{},Location:{}", itemId, locationId);

		String searchValue = request.search().value() == null ? "" : request.search().value().trim();
		// Get sorting
		String sortColumn = "itemName"; // default
		String sortDir = "asc";
		Pageable pageable = PageRequest.of(request.start() / request.length(), request.length(),
				Sort.Direction.fromString(sortDir.toUpperCase()), sortColumn);

		Page<StockMovementListView> page = stockMovementService.findMovementByItemAndLocations(itemId, locationId,
				searchValue, pageable);
		log.info("INFLOWS DATA: [{}]", page.getTotalElements()); // ← NOW YOU WILL SEE IT!
		return Map.of("draw", request.draw(), "recordsTotal", coreItemService.countAll(), "recordsFiltered",
				page.getTotalElements(), "data", page.getContent());
	}
}
