package com.niwe.erp.inventory.web.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.niwe.erp.common.util.DataParserUtil;
import com.niwe.erp.inventory.domain.WarehouseStock;
import com.niwe.erp.inventory.service.WarehouseStockService;
import com.niwe.erp.inventory.web.dto.ProductStockAgingDto;
import com.niwe.erp.inventory.web.dto.ProductStockSummaryDto;
import com.niwe.erp.inventory.web.dto.ProductStockValuationDto;
import com.niwe.erp.inventory.web.dto.WarehouseStockDetailDto;
import com.niwe.erp.inventory.web.util.NikaErpInventoryUrlConstants;
import com.niwe.erp.sale.service.excel.WarehouseStockExcelExportService;
import com.niwe.erp.sale.service.pdf.WarehouseStockPdfExportService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = NikaErpInventoryUrlConstants.WAREHOUSE_STOCKS_URL)
@AllArgsConstructor
public class WarehouseStockController {

	private final WarehouseStockService warehouseStockService;
	private final WarehouseStockExcelExportService warehouseStockExcelExportService;
	private final WarehouseStockPdfExportService warehouseStockPdfExportService;

	@GetMapping(path = "/list")
	public String listWarehouseStocks(Model model) {

		List<WarehouseStock> list = warehouseStockService.findAll();
		log.debug("--------------Calling listWarehouseStocks-------------------" + list.size());
		model.addAttribute("lists", list);
		return NikaErpInventoryUrlConstants.WAREHOUSE_STOCKS_LIST_PAGE;
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
		String fileName = DataParserUtil.dateFromInstant(Instant.now())+ "-stock-summary.xlsx";
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
		String fileName = DataParserUtil.dateFromInstant(Instant.now()) + "-stock-summary.pdf";
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attachment; filename=" + fileName);
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(in));

	}
	@GetMapping(path = "/aging/excel")
	public ResponseEntity<InputStreamResource> exportAgingToExcel(Model model) throws IOException {
		List<ProductStockAgingDto> list = warehouseStockService.getStockSummaryWithAging();
		ByteArrayInputStream in = warehouseStockExcelExportService.exportStockAgingToExcel(list);
		String fileName = DataParserUtil.dateFromInstant(Instant.now())+ "-stock-aging.xlsx";
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
		String fileName = DataParserUtil.dateFromInstant(Instant.now())+ "-stock-valuation.xlsx";
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
		String fileName = DataParserUtil.dateFromInstant(Instant.now()) + "-stock-valuation.pdf";
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attachment; filename=" + fileName);
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(in));

	}
}
