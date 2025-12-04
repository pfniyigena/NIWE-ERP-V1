package com.niwe.erp.inventory.web.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.niwe.erp.inventory.domain.InventoryLocation;
import com.niwe.erp.inventory.domain.Warehouse;
import com.niwe.erp.inventory.service.LocationService;
import com.niwe.erp.inventory.service.WarehouseService;
import com.niwe.erp.inventory.web.util.NikaErpInventoryUrlConstants;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = NikaErpInventoryUrlConstants.STANDS_URL)
@AllArgsConstructor
public class StandController {

	private final WarehouseService warehouseService;
	private final LocationService locationService;

	@GetMapping(path = "/list")
	public String listLocations(Model model) {
		List<InventoryLocation> list = locationService.findAll();
		log.debug("--------------Calling listLocations-------------------" + list.size());
		model.addAttribute("lists", list);
		return NikaErpInventoryUrlConstants.STANDS_LIST_URL;
	}

	@GetMapping(path = "/new")
	public String newTBranch(Model model) {
		setData(model);
		model.addAttribute("stand", InventoryLocation.builder().build());
		return NikaErpInventoryUrlConstants.STANDS_ADD_FORM_PAGE;
	}

	@PostMapping(path = "/new")
	public String saveStand(@Valid @ModelAttribute InventoryLocation stand, BindingResult bindingResult,
			RedirectAttributes redirectAttributes, Model model) {

		log.debug(String.format("------calling saveStand:{%s}", stand));
		if (bindingResult.hasErrors()) {
			model.addAttribute("stand", stand);
			setData(model);
			return NikaErpInventoryUrlConstants.STANDS_ADD_FORM_PAGE;
		}
		redirectAttributes.addFlashAttribute("success", "Success.");
		locationService.save(stand);
		return NikaErpInventoryUrlConstants.STANDS_LIST_REDITECT_URL;
	}

	@GetMapping(path = "/update/{id}")
	public String findById(@PathVariable String id, Model model) {
		InventoryLocation stand = locationService.findById(id);

		model.addAttribute("stand", stand);
		setData(model);
		return NikaErpInventoryUrlConstants.STANDS_ADD_FORM_PAGE;
	}

	private void setData(Model model) {
		List<Warehouse> warehouses = warehouseService.findAll();
		model.addAttribute("warehouses", warehouses);

	}
}
