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

import com.niwe.erp.core.domain.CoreCountry;
import com.niwe.erp.core.service.CoreCountryService;
import com.niwe.erp.core.web.util.NiweErpCoreUrlConstants;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = NiweErpCoreUrlConstants.COUNTRIES_URL)
@AllArgsConstructor
public class CoreCountryController {

	private final CoreCountryService coreCountryService;

	@GetMapping(path = "/list")
	public String listCountries(Model model) {

		List<CoreCountry> list = coreCountryService.findAll();
		log.debug("--------------Calling listCountries-------------------" + list.size());
		model.addAttribute("lists", list);
		return NiweErpCoreUrlConstants.COUNTRIES_LIST_PAGE;
	}

	@GetMapping(path = "/new")
	public String newCountry(Model model) {
		model.addAttribute("coreCountry", CoreCountry.builder().build());
		return NiweErpCoreUrlConstants.COUNTRIES_ADD_FORM_PAGE;
	}

	@PostMapping(path = "/new")
	public String saveCountry(@Valid CoreCountry coreCountry, BindingResult bindingResult,
			RedirectAttributes redirectAttributes, Model model) {

		log.debug(String.format("------calling saveCountry:{%s}", coreCountry));
		coreCountryService.save(coreCountry);
		redirectAttributes.addFlashAttribute("success", "Success.");
		return NiweErpCoreUrlConstants.COUNTRIES_LIST_REDITECT_URL;
	}

	@GetMapping(path = "/update/{id}")
	public String findById(@PathVariable String id, Model model) {
		CoreCountry coreCountry = coreCountryService.findById(id);

		model.addAttribute("coreCountry", coreCountry);

		return NiweErpCoreUrlConstants.COUNTRIES_ADD_FORM_PAGE;
	}

}
