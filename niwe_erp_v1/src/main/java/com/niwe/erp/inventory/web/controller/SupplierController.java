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

import com.niwe.erp.inventory.domain.Supplier;
import com.niwe.erp.inventory.service.SupplierService;
import com.niwe.erp.sale.web.util.NiweErpSaleUrlConstants;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = NiweErpSaleUrlConstants.SUPPLIERS_URL)
@AllArgsConstructor
@Slf4j
public class SupplierController {

	private final SupplierService supplierService;

	@GetMapping(path = "/list")
	public String listSuppliers(Model model) {

		List<Supplier> list = supplierService.findAll();
		log.debug("--------------Calling listSuppliers-------------------" + list.size());
		model.addAttribute("lists", list);
		return NiweErpSaleUrlConstants.SUPPLIERS_LIST_PAGE;
	}

	@GetMapping(path = "/new")
	public String newSupplier(Model model) {
		model.addAttribute("supplier", Supplier.builder().build());
		return NiweErpSaleUrlConstants.SUPPLIERS_ADD_FORM_PAGE;
	}

	@PostMapping(path = "/new")
	public String saveSupplier(@Valid Supplier supplier, BindingResult bindingResult,
			RedirectAttributes redirectAttributes, Model model) {

		if (bindingResult.hasErrors()) {
			model.addAttribute("supplier", supplier);
			return NiweErpSaleUrlConstants.SUPPLIERS_ADD_FORM_PAGE;
		}
		supplierService.save(supplier);
		redirectAttributes.addFlashAttribute("success", "Success.");
		return NiweErpSaleUrlConstants.SUPPLIERS_LIST_REDITECT_URL;
	}

	@GetMapping(path = "/update/{id}")
	public String findById(@PathVariable String id, Model model) {
		Supplier supplier = supplierService.findById(id);
		model.addAttribute("supplier", supplier);
		return NiweErpSaleUrlConstants.SUPPLIERS_ADD_FORM_PAGE;
	}
}
