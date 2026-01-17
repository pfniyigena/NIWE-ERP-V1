package com.niwe.erp.inventory.web.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.niwe.erp.core.service.CoreItemService;
import com.niwe.erp.core.service.CoreTaxpayerService;
import com.niwe.erp.core.web.ajax.DataTablesRequest;
import com.niwe.erp.inventory.domain.StockMovement;
import com.niwe.erp.inventory.service.StockMovementService;
import com.niwe.erp.inventory.service.excel.StockMovementExcelExportService;
import com.niwe.erp.inventory.service.pdf.StockMovementPdfExportService;
import com.niwe.erp.inventory.web.util.NikaErpInventoryUrlConstants;
import com.niwe.erp.inventory.web.view.StockMovementListView;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = NikaErpInventoryUrlConstants.STOCK_MOVEMENTS_URL)
@AllArgsConstructor
public class StockMovementController {
	private final CoreItemService coreItemService;
	private final StockMovementService stockMovementService;
	private final StockMovementExcelExportService stockMovementExcelExportService;
	private final StockMovementPdfExportService stockMovementPdfExportService;
	private final CoreTaxpayerService coreTaxpayerService;

	@GetMapping(path = "/list")
	public String listStockMovements(Model model) {

		List<StockMovement> list = stockMovementService.findAll();
		log.debug("--------------Calling listStockMovements-------------------" + list.size());
		model.addAttribute("lists", list);
		return NikaErpInventoryUrlConstants.STOCK_MOVEMENTS_LIST_PAGE;
	}

	@GetMapping(path = "/report")
	public String reportStockMovements(Model model) {

		return NikaErpInventoryUrlConstants.STOCK_MOVEMENTS_REPORT_PAGE;
	}

	@PostMapping("/ajax/view")
	@ResponseBody
	public Map<String, Object> getMovementsWithDate(@RequestBody DataTablesRequest request) {
		String searchValue = request.search().value() == null ? "" : request.search().value().trim();
		LocalDate fromDate = request.fromDate();
		LocalDate toDate = request.toDate();
		String movementType = request.movementType();
		log.info("getMovementsWithDate fromDate:{},toDate:{},movementType:{}", fromDate, toDate, movementType);

		// Get sorting
		String sortColumn = "modifiedAt"; // default
		String sortDir = "desc";
		Pageable pageable = PageRequest.of(request.start() / request.length(), request.length(),
				Sort.Direction.fromString(sortDir.toUpperCase()), sortColumn);

		Page<StockMovementListView> page = stockMovementService.getMovementsWithDate(searchValue, movementType,
				fromDate, toDate, pageable);
		log.info("INFLOWS DATA: [{}]", page.getTotalElements()); // ← NOW YOU WILL SEE IT!
		return Map.of("draw", request.draw(), "recordsTotal", coreItemService.countAll(), "recordsFiltered",
				page.getTotalElements(), "data", page.getContent());
	}

	@GetMapping("/export/excel")
	public ResponseEntity<InputStreamResource> exportToExcel(@RequestParam(required = false) String search,
			@RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate fromDate,
			@RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate toDate,
			@RequestParam(required = false) String movementType, Model model) throws IOException {
		
		log.info("exportToExcel search:{},fromDate:{},toDate:{},movementType:{}",search,fromDate,toDate,movementType);
		List<StockMovementListView> movements = stockMovementService.findWithFilters(search, movementType, fromDate,
				toDate, null);		ByteArrayInputStream in = stockMovementExcelExportService.exportMovementsToExcel(movements);
		String fileName = "movements.xlsx";
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attachment; filename=" + fileName);

		return ResponseEntity.ok().headers(headers)
				.contentType(
						MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
				.body(new InputStreamResource(in));
	}

	@GetMapping("/export/pdf")
	public ResponseEntity<InputStreamResource> exportToPdf(@RequestParam(required = false) String search,
			@RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate fromDate,
			@RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate toDate,
			@RequestParam(required = false) String movementType, Model model) throws IOException {
		log.info("exportToPdf search:{},fromDate:{},toDate:{},movementType:{}",search,fromDate,toDate,movementType);
		List<StockMovementListView> movements = stockMovementService.findWithFilters(search, movementType, fromDate,
				toDate, null);

		ByteArrayInputStream in = stockMovementPdfExportService.exportMovementsToPdf(movements,
				coreTaxpayerService.findAll().get(0));
		String fileName = "movements.pdf";
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attachment; filename=" + fileName);

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(in));

	}

}
