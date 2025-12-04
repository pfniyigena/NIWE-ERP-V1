package com.niwe.erp.invoicing.web.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.niwe.erp.invoicing.domain.TaxType;
import com.niwe.erp.invoicing.service.TaxTypeService;
import com.niwe.erp.invoicing.web.util.NikaErpInvoicingUrlConstants;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = NikaErpInvoicingUrlConstants.TAXTYPES_URL)
@AllArgsConstructor
public class TaxTypeController {

	private final TaxTypeService taxTypeService;

	@GetMapping(path = "/list")
	public String listTaxTypes(Model model) {
		List<TaxType> list = taxTypeService.findAll();
		log.debug("--------------Calling listTaxTypes-------------------:{}", list.size());
		model.addAttribute("lists", list);
		return NikaErpInvoicingUrlConstants.TAXTYPES_LIST_PAGE;
	}

	@GetMapping(path = "/new")
	public String newTaxType(Model model) {
		model.addAttribute("taxType", TaxType.builder().build());
		return NikaErpInvoicingUrlConstants.TAXTYPE_ADD_FORM;
	}

	@PostMapping(path = "/new")
	public String saveTaxType(@Valid TaxType taxType, BindingResult bindingResult, RedirectAttributes redirectAttrs,
			Model model) {

		log.debug(String.format("------calling saveTaxType:{%s}", taxType));
		taxTypeService.save(taxType);
		return NikaErpInvoicingUrlConstants.TAXTYPES_LIST_REDITECT_URL;
	}

	@GetMapping(path = "/update/{id}")
	public String findById(@PathVariable String id, Model model) {
		TaxType taxType = taxTypeService.findById(id);

		model.addAttribute("taxType", taxType);

		return NikaErpInvoicingUrlConstants.TAXTYPE_ADD_FORM;
	}

}
