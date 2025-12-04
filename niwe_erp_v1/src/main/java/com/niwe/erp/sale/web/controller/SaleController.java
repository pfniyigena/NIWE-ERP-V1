package com.niwe.erp.sale.web.controller;

import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.niwe.erp.core.web.ajax.DataTablesRequest;
import com.niwe.erp.sale.domain.Sale;
import com.niwe.erp.sale.service.SaleService;
import com.niwe.erp.sale.view.SaleListView;
import com.niwe.erp.sale.web.util.NiweErpSaleUrlConstants;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = NiweErpSaleUrlConstants.SALES_URL)
@AllArgsConstructor
@Slf4j
public class SaleController {
	private final SaleService saleService;

	@GetMapping(path = "/list-v2")
	public String listSalesV2(Model model) {

		List<Sale> list = saleService.findAll();
		log.debug("--------------Calling listSales-------------------:{}", list.size());
		model.addAttribute("lists", list);
		return NiweErpSaleUrlConstants.SALES_LIST_PAGE;
	}

	@GetMapping(path = "/list")
	public String listSales(Model model) {

		return NiweErpSaleUrlConstants.SALES_LIST_PAGE;
	}

	@PostMapping(value = "/list/data", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map<String, Object> getData(@RequestBody DataTablesRequest request) {
		log.info("SEARCH VALUE RECEIVED: [{}]", request.search().value());
		String searchValue = request.search().value() == null ? "" : request.search().value().trim();
		String sortColumn = "saleDate"; // default
		String sortDir = "asc";
		Pageable pageable = PageRequest.of(request.start() / request.length(), request.length(),
				Sort.Direction.fromString(sortDir.toUpperCase()), sortColumn);
		Page<SaleListView> salesPage = saleService.findAllSales(searchValue, pageable);
		log.info("DATA: [{}]", salesPage.getTotalElements()); // ← NOW YOU WILL SEE IT!
		return Map.of("draw", request.draw(), "recordsTotal", saleService.countAll(), "recordsFiltered",
				salesPage.getTotalElements(), "data", salesPage.getContent());
	}

	@GetMapping(path = "/view/{id}")
	public String viewItemInfo(@PathVariable String id, Model model) {
		Sale sale = saleService.findById(id);

		model.addAttribute("sale", sale);
		
		return NiweErpSaleUrlConstants.SALES_VIEW_PAGE;
	}
}
