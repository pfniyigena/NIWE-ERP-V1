package com.niwe.erp.core.web.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.niwe.erp.core.domain.CoreTaxpayer;
import com.niwe.erp.core.domain.EFiscalYear;
import com.niwe.erp.core.service.CoreTaxpayerService;
import com.niwe.erp.core.web.util.NiweErpCoreUrlConstants;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = NiweErpCoreUrlConstants.TAXPAYERS_URL)
@AllArgsConstructor
public class CoreTaxpayerController {

	private final CoreTaxpayerService coreTaxpayerService;

	@GetMapping(path = "/list")
	public String listCompanies(Model model) {

		List<CoreTaxpayer> list = coreTaxpayerService.findAll();
		log.debug("--------------Calling listCompanies-------------------" + list.size());
		model.addAttribute("lists", list);
		return NiweErpCoreUrlConstants.TAXPAYERS_LIST_PAGE;
	}

	@GetMapping(path = "/update/{id}")
	public String findById(@PathVariable String id, Model model) {
		CoreTaxpayer coreTaxpayer = coreTaxpayerService.findById(id);
		log.debug(String.format("------calling findById:{%s}", coreTaxpayer));
		model.addAttribute("coreTaxpayer", coreTaxpayer);
		setData(model);
		return NiweErpCoreUrlConstants.TAXPAYERS_ADD_FORM_PAGE;
	}

	@PostMapping(path = "/new")
	public String saveTaxpayer(@Valid CoreTaxpayer coreTaxpayer, BindingResult bindingResult,
			RedirectAttributes redirectAttributes, Model model) {

		log.debug(String.format("------calling saveTaxpayer:{%s}", coreTaxpayer));
		if (bindingResult.hasErrors()) {
			model.addAttribute("coreTaxpayer", coreTaxpayer);
			setData(model);
			return NiweErpCoreUrlConstants.TAXPAYERS_ADD_FORM_PAGE;
		}
		coreTaxpayerService.save(coreTaxpayer);
		redirectAttributes.addFlashAttribute("success", "Success.");

		return NiweErpCoreUrlConstants.TAXPAYER_LIST_REDITECT_URL;
	}

	private void setData(Model model) {
		model.addAttribute("fiscalYears", EFiscalYear.values());

	}

}
