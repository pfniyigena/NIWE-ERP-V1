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

import com.niwe.erp.invoicing.domain.ChargeType;
import com.niwe.erp.invoicing.service.ChargeTypeService;
import com.niwe.erp.invoicing.web.util.NikaErpInvoicingUrlConstants;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = NikaErpInvoicingUrlConstants.CHARGETYPES_URL)
@AllArgsConstructor
public class ChargeTypeController {

	private final ChargeTypeService chargeTypeService;

	@GetMapping(path = "/list")
	public String listTaxTypes(Model model) {
		List<ChargeType> list = chargeTypeService.findAll();
		log.debug("--------------Calling listTaxTypes-------------------:{}", list.size());
		model.addAttribute("lists", list);
		return NikaErpInvoicingUrlConstants.CHARGETYPES_LIST_PAGE;
	}

	@GetMapping(path = "/new")
	public String newTaxType(Model model) {
		model.addAttribute("chargeType", ChargeType.builder().build());
		return NikaErpInvoicingUrlConstants.CHARGETYPE_ADD_FORM;
	}

	@PostMapping(path = "/new")
	public String saveChargeType(@Valid ChargeType chargeType, BindingResult bindingResult, RedirectAttributes redirectAttrs,
			Model model) {
		log.debug(String.format("------calling saveChargeType:{%s}", chargeType));
		chargeTypeService.save(chargeType);
		return NikaErpInvoicingUrlConstants.CHARGETYPES_LIST_REDITECT_URL;
	}

	@GetMapping(path = "/update/{id}")
	public String findById(@PathVariable String id, Model model) {
		ChargeType chargeType = chargeTypeService.findById(id);
		model.addAttribute("chargeType", chargeType);
		return NikaErpInvoicingUrlConstants.CHARGETYPE_ADD_FORM;
	}

}
