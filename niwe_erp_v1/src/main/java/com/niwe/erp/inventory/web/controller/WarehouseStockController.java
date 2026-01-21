package com.niwe.erp.inventory.web.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.niwe.erp.common.util.DataParserUtil;
import com.niwe.erp.core.service.BrandService;
import com.niwe.erp.core.service.CategoryService;
import com.niwe.erp.core.service.CoreItemService;
import com.niwe.erp.core.service.CoreTaxpayerService;
import com.niwe.erp.core.web.ajax.WarehouseStockDataTablesRequest;
import com.niwe.erp.inventory.domain.WarehouseStock;
import com.niwe.erp.inventory.service.WarehouseStockService;
import com.niwe.erp.inventory.service.excel.WarehouseStockExcelExportService;
import com.niwe.erp.inventory.service.pdf.WarehouseStockPdfExportService;
import com.niwe.erp.inventory.web.dto.ProductStockAgingDto;
import com.niwe.erp.inventory.web.dto.ProductStockSummaryDto;
import com.niwe.erp.inventory.web.dto.ProductStockValuationDto;
import com.niwe.erp.inventory.web.dto.WarehouseStockDetailDto;
import com.niwe.erp.inventory.web.util.NikaErpInventoryUrlConstants;
import com.niwe.erp.inventory.web.view.InflowItemListView;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = NikaErpInventoryUrlConstants.WAREHOUSE_STOCKS_URL)
@AllArgsConstructor
public class WarehouseStockController {
	private final CoreItemService coreItemService;
	private final WarehouseStockService warehouseStockService;
	private final WarehouseStockExcelExportService warehouseStockExcelExportService;
	private final WarehouseStockPdfExportService warehouseStockPdfExportService;
	private final CategoryService categoryService;
	private final BrandService brandService;
	private final CoreTaxpayerService coreTaxpayerService;

	@GetMapping(path = "/list")
	public String listWarehouseStocks(Model model) {

		List<WarehouseStock> list = warehouseStockService.findAll();
		log.info("--------------Calling listWarehouseStocks-------------------" + list.size());
		model.addAttribute("lists", list);
		return NikaErpInventoryUrlConstants.WAREHOUSE_STOCKS_LIST_PAGE;
	}

	@GetMapping(path = "/report")
	public String listStockReport(Model model) {
		log.info("--------------Calling listStockReport-------------------");
		model.addAttribute("categories", categoryService.findAll());
		model.addAttribute("brands", brandService.findAll());
		return NikaErpInventoryUrlConstants.WAREHOUSE_STOCKS_REPORT_PAGE;
	}

	@GetMapping(path = "/summary")
	public String listWarehouseStockByProduct(Model model) {

		List<ProductStockSummaryDto> list = warehouseStockService.getStockSummary();
		log.debug("--------------Calling listWarehouseStockByProduct-------------------" + list.size());
		model.addAttribute("lists", list);
		return NikaErpInventoryUrlConstants.WAREHOUSE_STOCKS_SUMMARY_PAGE;
	}

	@GetMapping(path = "/aging")
	public String agingWarehouseStockByProduct(Model model) {

		List<ProductStockAgingDto> list = warehouseStockService.getStockSummaryWithAging();
		log.debug("--------------Calling agingtWarehouseStockByProduct-------------------" + list.size());
		model.addAttribute("lists", list);
		return NikaErpInventoryUrlConstants.WAREHOUSE_STOCKS_AGING_PAGE;
	}

	@GetMapping(path = "/valuation")
	public String valuationWarehouseStockByProduct(Model model) {

		List<ProductStockValuationDto> list = warehouseStockService.getStockValuationSummaryV2();
		log.debug("--------------Calling valuationWarehouseStockByProduct-------------------" + list.size());
		model.addAttribute("lists", list);
		return NikaErpInventoryUrlConstants.WAREHOUSE_STOCKS_VALUATION_PAGE;
	}

	@GetMapping(path = "/valuation/ajax")
	public String valuationWarehouseStockByProductAjax(Model model) {

		List<ProductStockValuationDto> list = warehouseStockService.getStockValuationSummaryV2();
		log.debug("--------------Calling valuationWarehouseStockByProduct-------------------" + list.size());
		model.addAttribute("lists", list);
		return NikaErpInventoryUrlConstants.WAREHOUSE_STOCKS_VALUATION_AJAX_PAGE;
	}

	@GetMapping("/warehouse/product/{id}")
	public String viewWarehouseStockByProduct(@PathVariable String id, Model model) {

		List<WarehouseStock> details = warehouseStockService.getStockByProduct(UUID.fromString(id));
		log.info("viewWarehouseStockByProduct details:{}", details);
		model.addAttribute("details", details);
		return NikaErpInventoryUrlConstants.WAREHOUSE_STOCKS_VIEW_FORM;
	}

	// Warehouse details view for a product
	@GetMapping("/details/{productId}")
	public String showWarehouseDetails(@PathVariable String productId, Model model) {
		List<WarehouseStockDetailDto> details = warehouseStockService.getWarehouseStockDetails(productId);
		model.addAttribute("details", details);
		return "inventory/details";
	}

	@GetMapping(path = "/summary/excel")
	public ResponseEntity<InputStreamResource> exportToExcel(Model model) throws IOException {
		List<ProductStockSummaryDto> list = warehouseStockService.getStockSummary();
		ByteArrayInputStream in = warehouseStockExcelExportService.exportSalesToExcel(list);
		String fileName = DataParserUtil.dateTimeFromInstant(Instant.now()) + "-stock-summary.xlsx";
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attachment; filename=" + fileName);
		return ResponseEntity.ok().headers(headers)
				.contentType(
						MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
				.body(new InputStreamResource(in));
	}

	@GetMapping(path = "/summary/pdf")
	public ResponseEntity<InputStreamResource> exportToPdf(Model model) throws IOException {
		List<ProductStockSummaryDto> list = warehouseStockService.getStockSummary();
		ByteArrayInputStream in = warehouseStockPdfExportService.exportStockSummaryToPdf(list);
		String fileName = DataParserUtil.dateTimeFromInstant(Instant.now()) + "-stock-summary.pdf";
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attachment; filename=" + fileName);
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(in));

	}

	@GetMapping(path = "/aging/excel")
	public ResponseEntity<InputStreamResource> exportAgingToExcel(Model model) throws IOException {
		List<ProductStockAgingDto> list = warehouseStockService.getStockSummaryWithAging();
		ByteArrayInputStream in = warehouseStockExcelExportService.exportStockAgingToExcel(list);
		String fileName = DataParserUtil.dateTimeFromInstant(Instant.now()) + "-stock-aging.xlsx";
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attachment; filename=" + fileName);
		return ResponseEntity.ok().headers(headers)
				.contentType(
						MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
				.body(new InputStreamResource(in));
	}

	@GetMapping(path = "/valuation/excel")
	public ResponseEntity<InputStreamResource> exportValuationToExcel(Model model) throws IOException {
		List<ProductStockValuationDto> list = warehouseStockService.getStockValuationSummaryV2();
		ByteArrayInputStream in = warehouseStockExcelExportService.exportEvaluationToExcel(list);
		String fileName = DataParserUtil.dateTimeFromInstant(Instant.now()) + "-stock-valuation.xlsx";
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attachment; filename=" + fileName);
		return ResponseEntity.ok().headers(headers)
				.contentType(
						MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
				.body(new InputStreamResource(in));
	}

	@GetMapping(path = "/valuation/pdf")
	public ResponseEntity<InputStreamResource> exportValuationToPdf(Model model) throws IOException {
		List<ProductStockValuationDto> list = warehouseStockService.getStockValuationSummary();
		ByteArrayInputStream in = warehouseStockPdfExportService.exportEvaluationToPdf(list);
		String fileName = DataParserUtil.dateTimeFromInstant(Instant.now()) + "-stock-valuation.pdf";
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attachment; filename=" + fileName);
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(in));

	}

	@PostMapping(value = "/ajax/view", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map<String, Object> getStockReportAjax(@RequestBody WarehouseStockDataTablesRequest request) {
		String searchValue = request.search().value() == null ? "" : request.search().value().trim();
		String category = request.categoryId() == null ? "" : request.categoryId().trim();
		String brand = request.brandId() == null ? "" : request.brandId().trim();
		log.info("getStockReportAjax: Search{},CategoryId:{},BrandId:{}", searchValue, category, brand);
		// Get sorting
		String sortColumn = "modifiedAt"; // default
		String sortDir = "desc";
		Pageable pageable = PageRequest.of(request.start() / request.length(), request.length(),
				Sort.Direction.fromString(sortDir.toUpperCase()), sortColumn);
		Page<InflowItemListView> itemsPage = warehouseStockService.findAllItemstock(category, brand, searchValue,
				pageable);
		log.info("STOCK REPORT DATA: [{}]", itemsPage.getTotalElements());
		return Map.of("draw", request.draw(), "recordsTotal", coreItemService.countAll(), "recordsFiltered",
				itemsPage.getTotalElements(), "data", itemsPage.getContent());
	}

	@GetMapping("/export/excel")
	public ResponseEntity<InputStreamResource> exportToExcel(@RequestParam(required = false) String search,
			@RequestParam(required = false) String categoryId, @RequestParam(required = false) String brandId,
			Model model) throws IOException {

		log.info("exportToExcel search:{},categoryId:{},brandId:{}", search, categoryId, brandId);
		Page<InflowItemListView> itemsPage = warehouseStockService.findAllItemstock(categoryId, brandId, search, null);
		ByteArrayInputStream in = warehouseStockExcelExportService.exportStocksToExcel(itemsPage.getContent());
		String fileName = DataParserUtil.dateTimeFromInstant(Instant.now()) + "stock.xlsx";
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attachment; filename=" + fileName);

		return ResponseEntity.ok().headers(headers)
				.contentType(
						MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
				.body(new InputStreamResource(in));
	}

	@GetMapping("/export/pdf")
	public ResponseEntity<InputStreamResource> exportToPdf(@RequestParam(required = false) String search,
			@RequestParam(required = false) String categoryId, @RequestParam(required = false) String brandId,
			Model model) throws IOException {
		log.info("exportToPdf search:{},categoryId:{},brandId:{}", search, categoryId, brandId);
		Page<InflowItemListView> itemsPage = warehouseStockService.findAllItemstock(categoryId, brandId, search, null);

		ByteArrayInputStream in = warehouseStockPdfExportService.exportMovementsToPdf(itemsPage.getContent(),
				coreTaxpayerService.findAll().get(0));

		String fileName = DataParserUtil.dateTimeFromInstant(Instant.now()) + "stock.pdf";
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attachment; filename=" + fileName);

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(in));

	}
}
