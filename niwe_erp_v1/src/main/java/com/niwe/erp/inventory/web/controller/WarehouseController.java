package com.niwe.erp.inventory.web.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.niwe.erp.core.domain.CoreBranch;
import com.niwe.erp.core.service.CoreBranchService;
import com.niwe.erp.inventory.domain.Warehouse;
import com.niwe.erp.inventory.service.WarehouseService;
import com.niwe.erp.inventory.web.util.NikaErpInventoryUrlConstants;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = NikaErpInventoryUrlConstants.WAREHOUSES_URL)
@AllArgsConstructor
public class WarehouseController {

	private final WarehouseService warehouseService;
	private final CoreBranchService coreBranchService;

	@GetMapping(path = "/list")
	public String listWarehouses(Model model) {

		List<Warehouse> list = warehouseService.findAll();
		log.debug("--------------Calling listWarehouses-------------------" + list.size());
		model.addAttribute("lists", list);
		return NikaErpInventoryUrlConstants.WAREHOUSES_LIST_PAGE;
	}

	@GetMapping(path = "/new")
	public String warehouseForm(Model model) {
		model.addAttribute("warehouse", Warehouse.builder().build());
		setData(model);

		return NikaErpInventoryUrlConstants.WAREHOUSES_ADD_FORM_PAGE;
	}

	@PostMapping(path = "/new")
	public String saveWarehouse(Warehouse warehouse, BindingResult bindingResult, RedirectAttributes redirectAttrs,
			Model model) {

		log.debug(String.format("------calling saveWarehouse:{%s}", warehouse));
		if (bindingResult.hasErrors()) {
			model.addAttribute("warehouse", warehouse);
			setData(model);
			return NikaErpInventoryUrlConstants.WAREHOUSES_ADD_FORM_PAGE;
		}
		warehouseService.save(warehouse);
		return NikaErpInventoryUrlConstants.WAREHOUSES_LIST_REDITECT_URL;
	}

	@GetMapping(path = "/update/{id}")
	public String findById(@PathVariable String id, Model model) {
		Warehouse warehouse = warehouseService.findById(id);
		model.addAttribute("warehouse", warehouse);
		setData(model);
		return NikaErpInventoryUrlConstants.WAREHOUSES_ADD_FORM_PAGE;
	}

	private void setData(Model model) {
		List<CoreBranch> branches = coreBranchService.findAll();
		model.addAttribute("branches", branches);

	}

}
