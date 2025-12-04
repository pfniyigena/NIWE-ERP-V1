package com.niwe.erp.sale.web.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.niwe.erp.sale.domain.DailySalesSummary;
import com.niwe.erp.sale.domain.Sale;
import com.niwe.erp.sale.service.DailySalesSummaryService;
import com.niwe.erp.sale.service.SaleService;
import com.niwe.erp.sale.service.excel.SaleExcelExportService;
import com.niwe.erp.sale.service.pdf.SalePdfExportService;
import com.niwe.erp.sale.web.util.NiweErpSaleUrlConstants;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = NiweErpSaleUrlConstants.DAILY_SALES_SUMMARY_URL)
@AllArgsConstructor
@Slf4j
public class DailySalesSummaryController {
	private final SaleService saleService;
	private final SaleExcelExportService saleExportService;
	private final SalePdfExportService salePdfExportService;
	private final DailySalesSummaryService dailySalesSummaryService;

	@GetMapping(path = "/daily")
	public String listDailySalesSummary(Model model) {

		List<DailySalesSummary> list = dailySalesSummaryService.findAll();
		log.debug("--------------Calling listDailySalesSummary-------------------:{}", list.size());

		model.addAttribute("summaryDate", LocalDate.now());
		model.addAttribute("lists", list);
		return NiweErpSaleUrlConstants.DAILY_SALES_SUMMARY_LIST_PAGE;
	}

	@GetMapping("/view/{id}")
	public String viewInvoice(@PathVariable String id, Model model) {
		DailySalesSummary summary = dailySalesSummaryService.findById(id);
		List<Sale> sales = saleService.findByDailySalesSummary(summary);
		model.addAttribute("summary", summary);
		model.addAttribute("sales", sales);
		// model.addAttribute("taxes", taxes);
		return NiweErpSaleUrlConstants.DAILY_SALES_SUMMARY_DATAILS_PAGE; // ✅ Print view
	}

	@GetMapping("/filter")
	public String filterDailySalesSummary(@RequestParam LocalDate summaryDate, Model model) {
		log.debug("--------------Calling filterDailySalesSummary-------------------:{}", summaryDate);

		DailySalesSummary summary = dailySalesSummaryService.findBySummaryDate(summaryDate);
		List<Sale> sales = saleService.findByDailySalesSummary(summary);
		model.addAttribute("summary", summary);
		model.addAttribute("sales", sales);
		return NiweErpSaleUrlConstants.DAILY_SALES_SUMMARY_DATAILS_PAGE; // ✅ Print view
	}

	@GetMapping("/export/excel/{id}")
	public ResponseEntity<InputStreamResource> exportToExcel(@PathVariable String id, Model model) throws IOException {
		DailySalesSummary summary = dailySalesSummaryService.findById(id);
		List<Sale> sales = saleService.findByDailySalesSummary(summary);
		ByteArrayInputStream in = saleExportService.exportSalesToExcel(sales);
		String fileName = summary.getSummaryDate().toString() + "-sales.xlsx";
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attachment; filename=" + fileName);

		return ResponseEntity.ok().headers(headers)
				.contentType(
						MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
				.body(new InputStreamResource(in));
	}

	@GetMapping("/export/pdf/{id}")
	public ResponseEntity<InputStreamResource> exportToPdf(@PathVariable String id, Model model) throws IOException {
		DailySalesSummary summary = dailySalesSummaryService.findById(id);

		ByteArrayInputStream in = salePdfExportService.exportSalesToPdf(summary);
		String fileName = summary.getSummaryDate().toString() + "-sales.pdf";
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attachment; filename=" + fileName);

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(in));

	}

	@GetMapping("/export/summary/pdf/{id}")
	public ResponseEntity<InputStreamResource> exportDailySalesSummaryToPdf(@PathVariable String id, Model model)
			throws IOException {
		DailySalesSummary summary = dailySalesSummaryService.findById(id);

		ByteArrayInputStream in = salePdfExportService.exportDailySalesSummaryToPdf(summary);
		String fileName = summary.getSummaryDate().toString() + "-daily-summary-sales.pdf";
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attachment; filename=" + fileName);

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(in));

	}

	@GetMapping(path = "/monthly")
	public Object listMonthlySalesSummary(@RequestParam(required = false, defaultValue = "") String monthParam,
			@RequestParam(defaultValue = "search", required = false) String action, Model model,
			HttpServletResponse response) throws Exception {

		log.debug("--------------Calling listMonthlySalesSummary-------------------:{}", monthParam);
		YearMonth yearMonth = (monthParam == null || monthParam.isEmpty()) ? YearMonth.now()
				: YearMonth.parse(monthParam);
		List<Sale> monthlySales = saleService.getSalesByMonth(yearMonth);
		if ("pdf".equals(action)) {
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "attachment; filename=monthly-report-" + yearMonth + ".pdf");
			salePdfExportService.generateMonthlyPdf(response.getOutputStream(), monthlySales, yearMonth);
			return null;
		}
		if ("excel".equals(action)) {
		    response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		    response.setHeader("Content-Disposition", "attachment; filename=monthly-report-" + yearMonth + ".xlsx");
			saleExportService.exportMonthlySalesToExcel(response.getOutputStream(), monthlySales, yearMonth);
			return null;
		}
		model.addAttribute("lists", monthlySales);
		model.addAttribute("period", yearMonth);
		model.addAttribute("monthParam", yearMonth);
		return NiweErpSaleUrlConstants.MONTHLY_SALES_REPORT_LIST_PAGE;
	}
}
